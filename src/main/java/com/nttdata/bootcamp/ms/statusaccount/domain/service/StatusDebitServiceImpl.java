package com.nttdata.bootcamp.ms.statusaccount.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.bootcamp.ms.statusaccount.domain.dto.BankAccount;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Debit;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.DebitReportDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.DebitResponse;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.MovDebitCreditDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Movement;
import com.nttdata.bootcamp.ms.statusaccount.enums.TypeCurrency;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.BankAccountRestClient;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.CreditRestClient;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.CustomerRestClient;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.DebitRestClient;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.TransactionsRestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StatusDebitServiceImpl implements  StatusDebitService{

    @Autowired
    BankAccountRestClient bankAccountRestClient;

    @Autowired
    DebitRestClient debitRestClient;

    @Autowired
    CustomerRestClient customerRestClient;
    
    @Autowired
    TransactionsRestClient transactionsRestClient;
    
    @Autowired
    CreditRestClient creditRestClient;

	@Override
	public Mono<DebitResponse> updateDebitDeposit(Debit debit, Integer id) {
		DebitResponse response = new DebitResponse();
		Date date = new Date();
		return debitRestClient.getFindId(id).flatMap(deb -> {
			Float mount=deb.getAvailableBalance()+debit.getAvailableBalance();
			deb.setAvailableBalance(mount);
			return debitRestClient.updateDebit(deb).flatMap(upd -> {
				return bankAccountRestClient.getFindId(upd.getAccountId().get(0)).flatMap(account -> {
					account.setAccountBalance(mount);
					return bankAccountRestClient.updateAccount(account).flatMap(x -> {
						response.setMessage("successful Deposit");
						response.setDebit(upd);
						Movement movement = new Movement();
						movement.setAccountId(id);
						movement.setAmount(debit.getAvailableBalance());
						movement.setCurrency(TypeCurrency.SOLES);
						movement.setCustomerId(deb.getCustomerId());
						movement.setProductType("CARD DEBIT");
						movement.setTransactionDate(date);
						movement.setTransactionType("DEPOSIT");
						return transactionsRestClient.saveTransaction(movement).flatMap(t1 -> {
							movement.setAccountId(x.getAccountId());
							movement.setProductType(x.getAccountType());
							return transactionsRestClient.saveTransaction(movement).flatMap(t2 -> {
								return Mono.just(response);
							});
							
						});
					});
				});
			});
		});
	}

	@Override
	public Mono<DebitResponse> updateDebitPay(Debit debit, Integer id) {
		DebitResponse response = new DebitResponse();
		Date date = new Date();
		return debitRestClient.getFindId(id).flatMap(deb -> {
			Integer idAcc = deb.getAccountId().get(0);

			response.setMessage("You don't have enough balance");
			response.setDebit(deb);
			return getAccountMount(deb,debit).flatMap(b -> {
				Float newMount = b.getAccountBalance()-debit.getAvailableBalance();
				b.setAccountBalance(newMount);
				return bankAccountRestClient.updateAccount(b).flatMap(x -> {
					response.setMessage("successful Pay");
					response.setDebit(deb);
					Movement movement = new Movement();
					movement.setAccountId(id);
					movement.setAmount(debit.getAvailableBalance());
					movement.setCurrency(TypeCurrency.SOLES);
					movement.setCustomerId(deb.getCustomerId());
					movement.setProductType("CARD DEBIT");
					movement.setTransactionDate(date);
					movement.setTransactionType("PAY");
					if(idAcc==x.getAccountId()) {
						deb.setAvailableBalance(newMount);
						return debitRestClient.updateDebit(deb).flatMap(upd -> {
							return transactionsRestClient.saveTransaction(movement).flatMap(t1 -> {
								movement.setAccountId(x.getAccountId());
								movement.setProductType(x.getAccountType());
								return transactionsRestClient.saveTransaction(movement).flatMap(t2 -> {
									return Mono.just(response);
								});
								
							});
						});
					}
					return transactionsRestClient.saveTransaction(movement).flatMap(t1 -> {
						movement.setAccountId(x.getAccountId());
						movement.setProductType(x.getAccountType());
						return transactionsRestClient.saveTransaction(movement).flatMap(t2 -> {
							return Mono.just(response);
						});
						
					});
					
				});
			}).defaultIfEmpty(response);
		});
	}

	@Override
	public Mono<DebitResponse> createDebit(Debit debit) {
		DebitResponse response = new DebitResponse();
		response.setMessage("Error creating debit card");
		return bankAccountRestClient.getFindId(debit.getAccountId().get(0)).flatMap(c -> {
			debit.setAvailableBalance(c.getAccountBalance());
			return debitRestClient.createDebit(debit).flatMap(d -> {
				response.setDebit(d);
				response.setMessage("Debit card created");
				return Mono.just(response);
			});
		});
	}
		
	@Override
    public Mono<MovDebitCreditDto> getDebitCredit(String idCustomer) {
        List<Movement> listDebit = new ArrayList<>();
        List<Movement> listCredits = new ArrayList<>();
        MovDebitCreditDto statusData = new MovDebitCreditDto();
        return getDebitCreditCard(idCustomer,"CARD DEBIT").collectList().flatMap(dat1 -> {
        	listDebit.addAll(dat1);
        	statusData.setDebit(listDebit);
        	return getDebitCreditCard(idCustomer,"CARD CREDIT").collectList().flatMap(dat2 ->{
        		listCredits.addAll(dat2);
        		statusData.setCredit(listCredits);
        		return customerRestClient.getFindCustomerId(idCustomer).flatMap(dat3 ->{
        			statusData.setConsumer(dat3);
        			return Mono.just(statusData);
        		});
        	});
        });
	}
	
	@Override
	public Mono<DebitReportDto> getReportDebit(Integer idDebit) {
		DebitReportDto response = new DebitReportDto();
        List<Movement> listTransactions = new ArrayList<>();
        return transactionsRestClient.getFindAccount(idDebit).collectList().flatMap(dat ->{
        	listTransactions.addAll(dat);
        	response.setMovement(listTransactions);
        	return debitRestClient.getFindId(idDebit).flatMap(dat2 ->{
        		response.setDebit(dat2);
        		return customerRestClient.getFindCustomerId(dat2.getCustomerId()).flatMap(dat3 ->{
        			response.setConsumer(dat3);
        			return Mono.just(response);
        		});
        	});
        });
	}
	
	private Mono<BankAccount> getAccountMount(Debit debit,Debit debitNew) {
		return bankAccountRestClient.getFindIdCustomer(debit.getCustomerId())
				.filter(d -> debit.getAccountId().contains(d.getAccountId()))
				.filter(d -> d.getAccountBalance()>=debitNew.getAvailableBalance())
				.next();

	}
	
	private Flux<Movement> getDebitCreditCard(String idCustomer, String typeCard) {
		return transactionsRestClient.getFindDebitCredit(idCustomer)
				.filter(d -> d.getProductType().equals(typeCard))
				.sort((o1, o2) -> o1.getTransactionDate().compareTo(o2.getTransactionDate()))
				.take(3);
	}

	
	
}
