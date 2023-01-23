package com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients;

import com.nttdata.bootcamp.ms.statusaccount.application.config.RestConfig;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.BankAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class BankAccountRestClient {
    RestConfig restConfig = new RestConfig();
    public Flux<BankAccount> getAccountByCustomer(Integer customerId){
        return restConfig.getWebClient("http://bankaccount:8093")
                .build()
                .get()
                .uri("/bankaccount/customer/" + customerId)
                .retrieve()
                .bodyToFlux(BankAccount.class);
    }
}
