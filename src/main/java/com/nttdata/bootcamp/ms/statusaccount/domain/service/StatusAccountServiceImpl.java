package com.nttdata.bootcamp.ms.statusaccount.domain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.bootcamp.ms.statusaccount.domain.dto.AccountMovementDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.BankAccount;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Credit;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.CreditMovementDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Movement;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.StatusAccountDto;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.BankAccountRestClient;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.CreditRestClient;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.CustomerRestClient;
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

	@Override
    public Mono<StatusAccountDto> getAllProducts(String dni, Integer idCustomer) {
        Map<String, Object> response = new HashMap<>();
        List<BankAccount> listAccounts = new ArrayList<>();
        List<Credit> listCredits = new ArrayList<>();
        StatusAccountDto statusData = new StatusAccountDto();
        return bankAccountRestClient.getFindIdCustomer(idCustomer).collectList().flatMap(dat ->{
        	listAccounts.addAll(dat);
        	System.out.println("monoFromFlux2 data: " + listAccounts.size());
        	response.put("accounts", listAccounts);
        	statusData.setAccounts(listAccounts);
        	return creditRestClient.getFindIdCustomer(idCustomer).collectList().flatMap(dat2 ->{
        		listCredits.addAll(dat2);
        		response.put("credits", listCredits);
        		statusData.setCredits(listCredits);
        		return customerRestClient.getFindId(dni).next().flatMap(dat3 ->{
        			response.put("customer", dat3);
        			statusData.setConsumer(dat3);
        			return Mono.just(statusData);
        		});
        	});
        });
	}
	
	/*Account consultation, deposit and withdrawal*/
	
	@Override
    public Mono<AccountMovementDto> getProductIdAccount(String dni,Integer idAccount) {
		AccountMovementDto response = new AccountMovementDto();
        List<BankAccount> listAccounts = new ArrayList<>();
        List<Movement> listTransactions = new ArrayList<>();
        return transactionsRestClient.getFindId(idAccount.toString()).collectList().flatMap(dat ->{
        	listTransactions.addAll(dat);
        	System.out.println("monoFromFlux2 data: " + listAccounts.size());
        	response.setMovement(listTransactions);
        	return bankAccountRestClient.getFindId(idAccount).flatMap(dat2 ->{
        		listAccounts.add(dat2);
        		response.setAccounts(listAccounts);
        		return customerRestClient.getFindId(dni).next().flatMap(dat3 ->{
        			response.setConsumer(dat3);
        			return Mono.just(response);
        		});
        	});
        });
	}

	@Override
	public Mono<Object> updateAccountDeposit(BankAccount account, Integer id) {
		Map<String, Object> response = new HashMap<>();
		return bankAccountRestClient.getFindId(id).flatMap(dat -> {
			Float mount=dat.getAccountBalance()+account.getAccountBalance();
			dat.setAccountBalance(mount);
			response.put("message", "successful Deposit");
			return bankAccountRestClient.updateAccount(dat).flatMap(c -> {
				response.put("account", c);
				return Mono.just(response);
			});
		});
	}

	@Override
	public Mono<Object> updateAccountWithdrawal(BankAccount account, Integer id) {
		Map<String, Object> response = new HashMap<>();
		return bankAccountRestClient.getFindId(id).flatMap(dat -> {
			Float mount=dat.getAccountBalance()-account.getAccountBalance();
			response.put("message", "You don't have enough balance");
			if(mount>=0) {
				response.put("message", "successful withdrawal");
				dat.setAccountBalance(mount);
			}
			return bankAccountRestClient.updateAccount(dat).flatMap(c -> {
				response.put("account", dat);
				return Mono.just(response);
			});
		});
	}
	
	/*credits consultation, deposit and withdrawal*/
	
	@Override
    public Mono<CreditMovementDto> getProductIdCredit(String dni,Integer idCredit) {
		CreditMovementDto response = new CreditMovementDto();
        List<Credit> listAccounts = new ArrayList<>();
        List<Movement> listTransactions = new ArrayList<>();
        return transactionsRestClient.getFindId(idCredit.toString()).collectList().flatMap(dat ->{
        	listTransactions.addAll(dat);
        	System.out.println("monoFromFlux2 data: " + listAccounts.size());
        	response.setMovement(listTransactions);
        	return creditRestClient.getFindId(idCredit).flatMap(dat2 ->{
        		listAccounts.add(dat2);
        		response.setCredit(listAccounts);
        		return customerRestClient.getFindId(dni).next().flatMap(dat3 ->{
        			response.setConsumer(dat3);
        			return Mono.just(response);
        		});
        	});
        });
	}
	
	@Override
	public Mono<Object> updateCreditPay(Credit credit, Integer id) {
		Map<String, Object> response = new HashMap<>();
		return creditRestClient.getFindId(id).flatMap(dat -> {
			Float mount=dat.getAvailableBalance()+credit.getAvailableBalance();
			response.put("message", "Payment exceeds the limit");
			if(dat.getCreditLimit()>=mount) {
				response.put("message", "successful pay");
				dat.setAvailableBalance(mount);
			}
			return creditRestClient.updateCredit(dat).flatMap(c -> {
				response.put("credit", dat);
				return Mono.just(response);
			});
		});
	}

	@Override
	public Mono<Object> updateCreditConsume(Credit credit, Integer id) {
		Map<String, Object> response = new HashMap<>();
		return creditRestClient.getFindId(id).flatMap(dat -> {
			Float mount=dat.getAvailableBalance()-credit.getAvailableBalance();
			response.put("message", "You don't have enough balance\"");
			if(mount>=0) {
				response.put("message", "successful consumption");
				dat.setAvailableBalance(mount);
			}
			return creditRestClient.updateCredit(dat).flatMap(c -> {
				response.put("credit", dat);
				return Mono.just(response);
			});
		});
	}
	
}
