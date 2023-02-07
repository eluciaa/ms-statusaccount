package com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Wallet;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.WalletDto;

import reactor.core.publisher.Mono;

@Service
public class WalletRestClient {
   
//    public Mono<WalletDto> getFindIdCustomer(String idCustomer){
//        WebClient webClient = WebClient.create("http://localhost:8094");
//
//        return  webClient.get()
//                .uri("/Wallet/customer/"+idCustomer)
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .retrieve()
//                .bodyToMono(WalletDto.class);
//    }
//    
    public Mono<Wallet> getFindId(Integer walletId){
        WebClient webClient = WebClient.create("http://localhost:8094");
        return  webClient.get()
                .uri("/monedero/"+walletId)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(Wallet.class);
    }
    
    public Mono<Wallet> createWallet(WalletDto wallet){
    	WebClient webClient = WebClient.create("http://localhost:8094");
        return  webClient.post()
                .uri("/monedero")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(wallet), WalletDto.class)
                .retrieve()
                .bodyToMono(Wallet.class);
    }
    
    public Mono<Wallet> walletDeposit(WalletDto wallet){
    	WebClient webClient = WebClient.create("http://localhost:8094");
        return  webClient.post()
                .uri("/monedero/deposit")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(wallet), WalletDto.class)
                .retrieve()
                .bodyToMono(Wallet.class);
    }
    
    public Mono<Wallet> walletPay(WalletDto wallet){
    	WebClient webClient = WebClient.create("http://localhost:8094");
        return  webClient.post()
                .uri("/monedero/payment")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(wallet), WalletDto.class)
                .retrieve()
                .bodyToMono(Wallet.class);
    }
}
