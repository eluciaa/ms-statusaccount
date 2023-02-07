package com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Credit;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditRestClient {
   
    public Flux<Credit> getFindIdCustomer(String idCustomer){
        WebClient webClient = WebClient.create("http://localhost:8086");

        return  webClient.get()
                .uri("/credits/customer/"+idCustomer)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(Credit.class);
    }
    
    public Mono<Credit> getFindId(Integer idCustomer){
        WebClient webClient = WebClient.create("http://localhost:8086");

        return  webClient.get()
                .uri("/credits/"+idCustomer)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(Credit.class);
    }
    
    public Mono<Credit> updateCredit(Credit credit){
    	WebClient webClient = WebClient.create("http://localhost:8086");

        return  webClient.put()
                .uri("/credits")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(credit), Credit.class)
                .retrieve()
                .bodyToMono(Credit.class);
    }
}
