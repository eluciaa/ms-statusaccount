package com.nttdata.bootcamp.ms.statusaccount.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.bootcamp.ms.statusaccount.domain.dto.AccountMovementDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.BankAccount;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Credit;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.CreditMovementDto;
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
    @GetMapping("/products/{dni}/{idCustomer}")
    public Mono<StatusAccountDto> getProductosIdCustomer(@PathVariable String dni, @PathVariable Integer idCustomer){
        return statusAccountService.getAllProducts(dni, idCustomer);
    }
    
    @GetMapping("/accountproduct/{dni}/{idAccount}")
    public Mono<AccountMovementDto> getProductIdAccount(@PathVariable String dni, @PathVariable Integer idAccount){
        return statusAccountService.getProductIdAccount(dni, idAccount);
    }
    
    @PutMapping("/accountdeposit/{id}")
	public Mono<Object> updateAccountDeposit(@RequestBody BankAccount account , @PathVariable Integer id){
    	return statusAccountService.updateAccountDeposit(account, id);
	}
    
    @PutMapping("/accountwithdrawal/{id}")
	public Mono<Object> updateAccountwithdrawal(@RequestBody BankAccount account , @PathVariable Integer id){
    	return statusAccountService.updateAccountWithdrawal(account, id);
	}
    
    
    @GetMapping("/creditproduct/{dni}/{idAccount}")
    public Mono<CreditMovementDto> getProductIdCredit(@PathVariable String dni, @PathVariable Integer idAccount){
        return statusAccountService.getProductIdCredit(dni, idAccount);
    }
    
    @PutMapping("/creditpay/{id}")
	public Mono<Object> updateCreditPay(@RequestBody Credit credit , @PathVariable Integer id){
    	return statusAccountService.updateCreditPay(credit, id);
	}
    
    @PutMapping("/creditconsume/{id}")
	public Mono<Object> updateCreditConsume(@RequestBody Credit credit , @PathVariable Integer id){
    	return statusAccountService.updateCreditConsume(credit, id);
	}

}
