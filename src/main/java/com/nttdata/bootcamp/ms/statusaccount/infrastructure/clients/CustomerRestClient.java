package com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients;

import com.nttdata.bootcamp.ms.statusaccount.application.config.RestConfig;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Credit;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class CustomerRestClient {
    RestConfig restConfig = new RestConfig();
    public Flux<Customer> getCustomerById(String dni){
        return restConfig.getWebClient("http://localhost:8082")
                .build()
                .get()
                .uri("/customer/findByClient/" + dni)
                .retrieve()
                .bodyToFlux(Customer.class);
    }
}
