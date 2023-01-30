package com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nttdata.bootcamp.ms.statusaccount.application.config.RestConfig;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Movement;

import reactor.core.publisher.Flux;

@Service
public class TransactionsRestClient {
    RestConfig restConfig = new RestConfig();
    
    public Flux<Movement> getFindId(String id){
        WebClient webClient = WebClient.create("http://localhost:808");

        return  webClient.get()
                .uri("/movement/"+id)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(Movement.class);

    }


}
