package com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients;

import com.nttdata.bootcamp.ms.statusaccount.application.config.RestConfig;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Credit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class CreditRestClient {

    RestConfig restConfig = new RestConfig();
    public Flux<Credit> getCreditByCustomer(Integer customerId){
        return restConfig.getWebClient("http://localhost:8092")
                .build()
                .get()
                .uri("/credits/customer/" + customerId)
                .retrieve()
                .bodyToFlux(Credit.class);
    }
}
