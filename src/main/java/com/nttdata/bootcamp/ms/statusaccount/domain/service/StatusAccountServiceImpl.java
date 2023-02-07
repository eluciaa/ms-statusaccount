package com.nttdata.bootcamp.ms.statusaccount.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.bootcamp.ms.statusaccount.domain.dto.AccountDepWitDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.AccountMovementDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.BankAccount;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Credit;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.CreditMovementDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.CreditPayConDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.CreditReportDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.DateConsultDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Movement;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.StatusAccountDto;
import com.nttdata.bootcamp.ms.statusaccount.enums.TypeCurrency;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.BankAccountRestClient;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.CreditRestClient;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.CustomerRestClient;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.DebitRestClient;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.TransactionsRestClient;

import reactor.core.publisher.Mono;

@Service
public class StatusAccountServiceImpl implements  StatusAccountService{

    @Autowired
    BankAccountRestClient bankAccountRestClient;

    @Autowired
    CreditRestClient creditRestClient;

    @Autowired
    CustomerRestClient customerRestClient;
    
    @Autowired
    TransactionsRestClient transactionsRestClient;
    
    @Autowired
    DebitRestClient debitRestClient;

	@Override
    public Mono<StatusAccountDto> getAllProducts(String idCustomer) {
        List<BankAccount> listAccounts = new ArrayList<>();
        List<Credit> listCredits = new ArrayList<>();
        StatusAccountDto statusData = new StatusAccountDto();
        return bankAccountRestClient.getFindIdCustomer(idCustomer).collectList().flatMap(dat ->{
        	listAccounts.addAll(dat);
        	statusData.setAccounts(listAccounts);
        	return creditRestClient.getFindIdCustomer(idCustomer).collectList().flatMap(dat2 ->{
        		listCredits.addAll(dat2);
        		statusData.setCredits(listCredits);
        		return customerRestClient.getFindCustomerId(idCustomer).flatMap(dat3 ->{
        			statusData.setConsumer(dat3);
        			return debitRestClient.getFindIdCustomer(idCustomer).flatMap(dat4 ->{
        				statusData.setDebit(dat4);
        				return Mono.just(statusData);
        			});
        		});
        	});
        });
	}
	
	/*Account consultation, deposit and withdrawal*/
	
	@Override
    public Mono<AccountMovementDto> getProductIdAccount(Integer idAccount) {
		AccountMovementDto response = new AccountMovementDto();
        List<BankAccount> listAccounts = new ArrayList<>();
        List<Movement> listTransactions = new ArrayList<>();
        return transactionsRestClient.getFindAccount(idAccount).collectList().flatMap(dat ->{
        	listTransactions.addAll(dat);
        	response.setMovement(listTransactions);
        	return bankAccountRestClient.getFindId(idAccount).flatMap(dat2 ->{
        		listAccounts.add(dat2);
        		response.setAccounts(listAccounts);
        		return customerRestClient.getFindCustomerId(dat2.getCustomerId()).flatMap(dat3 ->{
        			response.setConsumer(dat3);
        			return Mono.just(response);
        		});
        	});
        });
	}

	@Override
	public Mono<AccountDepWitDto> updateAccountDeposit(BankAccount account, Integer id) {
		AccountDepWitDto response = new AccountDepWitDto();
		Date date = new Date();
		return bankAccountRestClient.getFindId(id).flatMap(dat -> {
			Float mount=dat.getAccountBalance()+account.getAccountBalance();
			dat.setAccountBalance(mount);
			response.setMessage("successful Deposit");
			return bankAccountRestClient.updateAccount(dat).flatMap(c -> {
				response.setAccount(c);
				Movement movement = new Movement();
				movement.setAccountId(c.getAccountId());
				movement.setAmount(account.getAccountBalance());
				movement.setCurrency(TypeCurrency.SOLES);
				movement.setCustomerId(c.getCustomerId());
				movement.setProductType(c.getAccountType());
				movement.setTransactionDate(date);
				movement.setTransactionType("DEPOSIT");
				return transactionsRestClient.saveTransaction(movement).flatMap(t1 -> {
					return Mono.just(response);
				});
			});
		});
	}

	@Override
	public Mono<AccountDepWitDto> updateAccountWithdrawal(BankAccount account, Integer id) {
		AccountDepWitDto response = new AccountDepWitDto();
		Date date = new Date();
		return bankAccountRestClient.getFindId(id).flatMap(dat -> {
			Float mount=dat.getAccountBalance()-account.getAccountBalance();
			response.setMessage("You don't have enough balance");
			response.setAccount(dat);
			if(mount>=0) {
				response.setMessage("successful withdrawal");
				dat.setAccountBalance(mount);
				return bankAccountRestClient.updateAccount(dat).flatMap(c -> {
					response.setAccount(dat);
					Movement movement = new Movement();
					movement.setAccountId(c.getAccountId());
					movement.setAmount(account.getAccountBalance());
					movement.setCurrency(TypeCurrency.SOLES);
					movement.setCustomerId(c.getCustomerId());
					movement.setProductType(c.getAccountType());
					movement.setTransactionDate(date);
					movement.setTransactionType("WITHDRAWAL");
					return transactionsRestClient.saveTransaction(movement).flatMap(t1 -> {
						return Mono.just(response);
					});
				});
			}
			return Mono.just(response);
		});
	}
	
	/*credits consultation, deposit and withdrawal*/
	
	@Override
    public Mono<CreditMovementDto> getProductIdCredit(Integer idCredit) {
		CreditMovementDto response = new CreditMovementDto();
        List<Credit> listAccounts = new ArrayList<>();
        List<Movement> listTransactions = new ArrayList<>();
        return transactionsRestClient.getFindAccount(idCredit).collectList().flatMap(dat ->{
        	listTransactions.addAll(dat);
        	response.setMovement(listTransactions);
        	return creditRestClient.getFindId(idCredit).flatMap(dat2 ->{
        		listAccounts.add(dat2);
        		response.setCredit(listAccounts);
        		return customerRestClient.getFindCustomerId(dat2.getCustomerId()).flatMap(dat3 ->{
        			response.setConsumer(dat3);
        			return Mono.just(response);
        		});
        	});
        });
	}
	
	@Override
	public Mono<CreditPayConDto> updateCreditPay(Credit credit, Integer id) {
		CreditPayConDto response = new CreditPayConDto();
		Date date = new Date();
		return creditRestClient.getFindId(id).flatMap(dat -> {
			Float mount=dat.getAvailableBalance()+credit.getAvailableBalance();
			response.setMessage("Payment exceeds the limit");
			response.setCredit(dat);
			if(dat.getCreditLimit()>=mount) {
				response.setMessage("successful pay");
				dat.setAvailableBalance(mount);
				return creditRestClient.updateCredit(dat).flatMap(c -> {
					response.setCredit(dat);
					Movement movement = new Movement();
					movement.setAccountId(c.getId());
					movement.setAmount(credit.getAvailableBalance());
					movement.setCurrency(TypeCurrency.SOLES);
					movement.setCustomerId(c.getCustomerId());
					movement.setProductType(c.getCreditType());
					movement.setTransactionDate(date);
					movement.setTransactionType("PAY");
					return transactionsRestClient.saveTransaction(movement).flatMap(t1 -> {
						return Mono.just(response);
					});
			
				});
			}
			return Mono.just(response);
		});
	}

	@Override
	public Mono<CreditPayConDto> updateCreditConsume(Credit credit, Integer id) {
		CreditPayConDto response = new CreditPayConDto();
		Date date = new Date();
		return creditRestClient.getFindId(id).flatMap(dat -> {
			Float mount=dat.getAvailableBalance()-credit.getAvailableBalance();
			response.setMessage("You don't have enough balance\"");
			response.setCredit(dat);
			if(mount>=0) {
				dat.setAvailableBalance(mount);
				return creditRestClient.updateCredit(dat).flatMap(c -> {
					response.setMessage("successful consumption");
					response.setCredit(dat);
					Movement movement = new Movement();
					movement.setAccountId(c.getId());
					movement.setAmount(credit.getAvailableBalance());
					movement.setCurrency(TypeCurrency.SOLES);
					movement.setCustomerId(c.getCustomerId());
					movement.setProductType(c.getCreditType());
					movement.setTransactionDate(date);
					movement.setTransactionType("CONSUME");
					return transactionsRestClient.saveTransaction(movement).flatMap(t1 -> {
						return Mono.just(response);
					});
				});
			}
			return Mono.just(response);
		});
	}

	@Override
	public Mono<CreditReportDto> getReportCredit(DateConsultDto dateConsult, Integer idCredit) {
		CreditReportDto response = new CreditReportDto();
        List<Movement> listTransactions = new ArrayList<>();
        return transactionsRestClient.getFindAccount(idCredit)
        		.filter(f -> f.getTransactionDate().compareTo(dateConsult.getDateEnd())<0)
        		.filter(f -> f.getTransactionDate().compareTo(dateConsult.getDateStart())>=0)
        		.collectList().flatMap(dat ->{
        	listTransactions.addAll(dat);
        	response.setMovement(listTransactions);
        	return creditRestClient.getFindId(idCredit).flatMap(dat2 ->{
         		response.setCredit(dat2);
        		return customerRestClient.getFindCustomerId(dat2.getCustomerId()).flatMap(dat3 ->{
        			response.setConsumer(dat3);
        			return Mono.just(response);
        		});
        	});
        });
	}

}
