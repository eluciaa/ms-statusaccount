package com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Debit;

import reactor.core.publisher.Mono;

@Service
public class DebitRestClient {
   
    public Mono<Debit> getFindIdCustomer(String idCustomer){
        WebClient webClient = WebClient.create("http://localhost:8092");

        return  webClient.get()
                .uri("/debit/customer/"+idCustomer)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(Debit.class);
    }
    
    public Mono<Debit> getFindId(Integer idCustomer){
        WebClient webClient = WebClient.create("http://localhost:8092");

        return  webClient.get()
                .uri("/debit/"+idCustomer)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(Debit.class);
    }
    
    public Mono<Debit> updateDebit(Debit debit){
    	WebClient webClient = WebClient.create("http://localhost:8092");

        return  webClient.put()
                .uri("/debit")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(debit), Debit.class)
                .retrieve()
                .bodyToMono(Debit.class);
    }
    
    public Mono<Debit> createDebit(Debit debit){
    	WebClient webClient = WebClient.create("http://localhost:8092");

        return  webClient.post()
                .uri("/debit")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(debit), Debit.class)
                .retrieve()
                .bodyToMono(Debit.class);
    }
}
