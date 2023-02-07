package com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nttdata.bootcamp.ms.statusaccount.application.config.RestConfig;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.BankAccount;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BankAccountRestClient {
    RestConfig restConfig = new RestConfig();
    
    public Flux<BankAccount> getAccountByCustomer(Integer customerId){
        return restConfig.getWebClient("http://bankaccount:8700")
                .build()
                .get()
                .uri("/bankaccount/customer/" + customerId)
                .retrieve()
                .bodyToFlux(BankAccount.class);
    }
    
    // METODO UTILIZADO PARA REGISTRAR LA TRANSACCION EN EL MICROSERVICIO DE TRANSACCIONES
    public Mono<BankAccount> getBankAccount(BankAccount transactionCreateRequest){
        WebClient webClient = WebClient.create("http://localhost:8700");

        return  webClient.post()
                .uri("/accounts/account/"+transactionCreateRequest.getCustomerId())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(transactionCreateRequest), BankAccount.class)
                .retrieve().bodyToMono(BankAccount.class);

    }
    
    public Flux<BankAccount> getFindIdCustomer(String idCustomer){
        WebClient webClient = WebClient.create("http://localhost:8700");

        return  webClient.get()
                .uri("/accounts/customer/"+idCustomer)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(BankAccount.class);

    }
    
    public Mono<BankAccount> getFindId(Integer id){
        WebClient webClient = WebClient.create("http://localhost:8700");

        return  webClient.get()
                .uri("/accounts/account/"+id)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(BankAccount.class);

    }

    public Mono<BankAccount> updateAccount(BankAccount bankAccount){
    	WebClient webClient = WebClient.create("http://localhost:8700");

        return  webClient.put()
                .uri("/accounts")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(bankAccount), BankAccount.class)
                .retrieve()
                .bodyToMono(BankAccount.class);

    }


}
