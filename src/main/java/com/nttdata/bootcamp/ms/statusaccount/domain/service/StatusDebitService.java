package com.nttdata.bootcamp.ms.statusaccount.domain.service;

import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Debit;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.DebitReportDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.DebitResponse;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.MovDebitCreditDto;

import reactor.core.publisher.Mono;

public interface StatusDebitService {

	Mono<DebitResponse> updateDebitDeposit(Debit debit, Integer id);

	Mono<DebitResponse> updateDebitPay(Debit debit, Integer id);

	Mono<DebitResponse> createDebit(Debit debit);

	Mono<MovDebitCreditDto> getDebitCredit(String idCustomer);

	Mono<DebitReportDto> getReportDebit(Integer idCustomer);

}
