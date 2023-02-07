package com.nttdata.bootcamp.ms.statusaccount.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.bootcamp.ms.statusaccount.domain.dto.AccountDepWitDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.AccountMovementDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.BankAccount;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Credit;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.CreditMovementDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.CreditPayConDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.CreditReportDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.DateConsultDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.StatusAccountDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.service.StatusAccountService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/status")
@RequiredArgsConstructor
public class StatusAccountController {

	private final StatusAccountService statusAccountService;

    /**
     * Método que devuelve todos los productos que tiene el cliente
     * @param idCustomer
     * @return
     */
    @GetMapping("/products/{idCustomer}")
    public Mono<StatusAccountDto> getProductosIdCustomer(@PathVariable String idCustomer) {
        return statusAccountService.getAllProducts(idCustomer);
    }
    
    @GetMapping("/accountreport/{idAccount}")
    public Mono<AccountMovementDto> getProductIdAccount(@PathVariable Integer idAccount) {
        return statusAccountService.getProductIdAccount(idAccount);
    }
    
    @PutMapping("/accountdeposit/{id}")
    public Mono<AccountDepWitDto> updateAccountDeposit(@RequestBody BankAccount account , @PathVariable Integer id) {
        return statusAccountService.updateAccountDeposit(account, id);
	}
    
    @PutMapping("/accountwithdrawal/{id}")
    public Mono<AccountDepWitDto> updateAccountwithdrawal(@RequestBody BankAccount account , @PathVariable Integer id) {
        return statusAccountService.updateAccountWithdrawal(account, id);
	}
    
    
    @GetMapping("/creditreport/{idAccount}")
    public Mono<CreditMovementDto> getProductIdCredit(@PathVariable Integer idAccount) {
        return statusAccountService.getProductIdCredit(idAccount);
    }
    
    @PutMapping("/creditpay/{id}")
    public Mono<CreditPayConDto> updateCreditPay(@RequestBody Credit credit , @PathVariable Integer id) {
        return statusAccountService.updateCreditPay(credit, id);
	}
    
    @PutMapping("/creditconsume/{id}")
    public Mono<CreditPayConDto> updateCreditConsume(@RequestBody Credit credit , @PathVariable Integer id) {
        return statusAccountService.updateCreditConsume(credit, id);
	}
    
    @GetMapping("/reportcredit/{idCredit}")
    public Mono<CreditReportDto> getReportCredit(@RequestBody DateConsultDto dateConsult, @PathVariable Integer idCredit) {
        return statusAccountService.getReportCredit(dateConsult, idCredit);
    }

}
