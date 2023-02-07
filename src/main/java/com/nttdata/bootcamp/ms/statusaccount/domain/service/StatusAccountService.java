package com.nttdata.bootcamp.ms.statusaccount.domain.service;

import com.nttdata.bootcamp.ms.statusaccount.domain.dto.AccountDepWitDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.AccountMovementDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.BankAccount;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Credit;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.CreditMovementDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.CreditPayConDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.CreditReportDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.DateConsultDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.StatusAccountDto;

import reactor.core.publisher.Mono;

public interface StatusAccountService {

	Mono<StatusAccountDto> getAllProducts(String idCustomer);
	
	Mono<AccountMovementDto> getProductIdAccount(Integer idAccount);

	Mono<AccountDepWitDto> updateAccountDeposit(BankAccount account, Integer id);

	Mono<AccountDepWitDto> updateAccountWithdrawal(BankAccount account, Integer id);
	

	Mono<CreditMovementDto> getProductIdCredit(Integer idCredit);

	Mono<CreditPayConDto> updateCreditPay(Credit credit, Integer id);

	Mono<CreditPayConDto> updateCreditConsume(Credit credit, Integer id);

	Mono<CreditReportDto> getReportCredit(DateConsultDto dateConsult, Integer idCredit);

}
