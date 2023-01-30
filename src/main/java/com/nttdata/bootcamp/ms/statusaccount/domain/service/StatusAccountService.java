package com.nttdata.bootcamp.ms.statusaccount.domain.service;

import com.nttdata.bootcamp.ms.statusaccount.domain.dto.AccountMovementDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.BankAccount;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Credit;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.CreditMovementDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.StatusAccountDto;

import reactor.core.publisher.Mono;

public interface StatusAccountService {

	Mono<StatusAccountDto> getAllProducts(String dni, Integer idCustomer);
	
	Mono<AccountMovementDto> getProductIdAccount(String dni, Integer idAccount);

	Mono<Object> updateAccountDeposit(BankAccount account, Integer id);

	Mono<Object> updateAccountWithdrawal(BankAccount account, Integer id);
	

	Mono<CreditMovementDto> getProductIdCredit(String dni, Integer idCredit);

	Mono<Object> updateCreditPay(Credit credit, Integer id);

	Mono<Object> updateCreditConsume(Credit credit, Integer id);
}
