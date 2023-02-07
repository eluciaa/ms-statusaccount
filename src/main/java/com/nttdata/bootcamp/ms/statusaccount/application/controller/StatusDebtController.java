package com.nttdata.bootcamp.ms.statusaccount.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Debit;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.DebitReportDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.DebitResponse;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.MovDebitCreditDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.service.StatusDebitService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/status")
@RequiredArgsConstructor
public class StatusDebtController {

	private final StatusDebitService statusDebitService;
	
	@PostMapping("/debitcreate")
	public Mono<DebitResponse> createDebit(@RequestBody Debit debit){
    	return statusDebitService.createDebit(debit);
	}

    @PutMapping("/debitdeposit/{id}")
	public Mono<DebitResponse> updateDebitDeposit(@RequestBody Debit debit , @PathVariable Integer id){
    	return statusDebitService.updateDebitDeposit(debit, id);
	}
    
    @PutMapping("/debitpay/{id}")
	public Mono<DebitResponse> updateDebitPay(@RequestBody Debit debit , @PathVariable Integer id){
    	return statusDebitService.updateDebitPay(debit, id);
	}
    
    @GetMapping("/debitcredit/{idCustomer}")
    public Mono<MovDebitCreditDto> getDebitCredit(@PathVariable String idCustomer){
        return statusDebitService.getDebitCredit(idCustomer);
    }
    
    @GetMapping("/debitreport/{idDebit}")
    public Mono<DebitReportDto> getReportDebit(@PathVariable Integer idDebit){
        return statusDebitService.getReportDebit(idDebit);
    }

}
