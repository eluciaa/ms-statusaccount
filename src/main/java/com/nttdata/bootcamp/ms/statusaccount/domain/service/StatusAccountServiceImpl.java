package com.nttdata.bootcamp.ms.statusaccount.domain.service;

import com.google.gson.Gson;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Customer;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.StatusAccountDto;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.BankAccountRestClient;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.CreditRestClient;
import com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients.CustomerRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class StatusAccountServiceImpl implements  StatusAccountService{

    @Autowired
    BankAccountRestClient bankAccountRestClient;

    @Autowired
    CreditRestClient creditRestClient;

    @Autowired
    CustomerRestClient customerRestClient;

    @Override
    public Mono<StatusAccountDto> getAllProducts(String dni, Integer idCustomer) {
        StatusAccountDto statusAccountDto = new StatusAccountDto();

        statusAccountDto.setConsumer(customerRestClient.getCustomerById(dni));
        log.info("Consumer asignado");
        statusAccountDto.setCredits(creditRestClient.getCreditByCustomer(idCustomer));
        log.info("Credito asignado");
        statusAccountDto.setAccounts(bankAccountRestClient.getAccountByCustomer(idCustomer));
        log.info("BankAccount asignado");
        Gson gson = new Gson();
        log.info(gson.toJson(statusAccountDto));
        return Mono.just(statusAccountDto);
    }

    private Flux<Customer> getCustomerById(String dni) {
        Flux<Customer> customerMono = customerRestClient.getCustomerById(dni);
        customerMono.subscribe();
        return customerMono;
    }
}
