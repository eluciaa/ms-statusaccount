package com.nttdata.bootcamp.ms.statusaccount.application.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.bootcamp.ms.statusaccount.domain.dto.WalletDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.WalletResponse;
import com.nttdata.bootcamp.ms.statusaccount.domain.service.StatusWalletService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/status")
@RequiredArgsConstructor
public class StatusWalletController {

	private final StatusWalletService statusWalletService;
	
	@PostMapping("/walletcreate")
	public Mono<WalletResponse> createWallet(@RequestBody WalletDto wallet){
    	return statusWalletService.createWallet(wallet);
	}

    @PutMapping("/walletdeposit")
	public Mono<WalletResponse> walletDeposit(@RequestBody WalletDto wallet){
    	return statusWalletService.walletDeposit(wallet);
	}
    
    @PutMapping("/walletpay")
	public Mono<WalletResponse> walletPay(@RequestBody WalletDto wallet){
    	return statusWalletService.walletPay(wallet);
	}
    
//    @PutMapping("/debitpay/{id}")
//	public Mono<DebitResponse> updateDebitPay(@RequestBody Debit debit , @PathVariable Integer id){
//    	return statusWalletService.updateDebitPay(debit, id);
//	}
//    
//    @GetMapping("/debitcredit/{idCustomer}")
//    public Mono<MovDebitCreditDto> getDebitCredit(@PathVariable String idCustomer){
//        return statusWalletService.getDebitCredit(idCustomer);
//    }
//    
//    @GetMapping("/debitreport/{idDebit}")
//    public Mono<DebitReportDto> getReportDebit(@PathVariable Integer idDebit){
//        return statusWalletService.getReportDebit(idDebit);
//    }

}
