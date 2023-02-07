package com.nttdata.bootcamp.ms.statusaccount.rest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.nttdata.bootcamp.ms.statusaccount.application.controller.StatusAccountController;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.AccountMovementDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.BankAccount;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Credit;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.CreditMovementDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Customer;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Debit;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Movement;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.StatusAccountDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.service.StatusAccountService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class StatusAccountControllerTest {
	
	private static WebTestClient webClient;
	
	@Mock
	private static StatusAccountService service = mock(StatusAccountService.class);
	
	@Test
    void getAccountById(){
		
		List<BankAccount> account = Arrays.asList(new BankAccount(3,"0011-1235-3453-1245",5000.0f,"63dafea9d8a3387f0d8613f2","AHORRO"));
		List<Credit> credit = Arrays.asList(new Credit(2, "0112-1235-3453-0002", 5000.0f, "10",10.0f,"63dafea9d8a3387f0d8613f2","CARD CREDIT"));
		Customer customer = new Customer("63d17a46f4a3a745571eef09", "12345678", "PERSONAL", false, false, "Alex", "Apellido", "direccion", "ACTIVE", null, null);
		Debit debit = new Debit(2,"0011-1235-3453-1245","30",5000.0f,Arrays.asList(1, 3),"63dafea9d8a3387f0d8613f2");
		StatusAccountDto statusData = new StatusAccountDto(customer, account, credit,debit);
		
        Mono<StatusAccountDto> statusDataMono = Mono.just(new StatusAccountDto(customer, account,
        		credit,debit));

        when (service.getAllProducts(statusDataMono.block().getConsumer().getId()))
                .thenReturn(statusDataMono);

        webClient = WebTestClient.bindToController(new StatusAccountController(service))
				.configureClient()
				.baseUrl("/status/products/"+statusDataMono.block().getConsumer().getId())
				.build();
        
        webClient.get()
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(StatusAccountDto.class)
                .getResponseBody();

        StepVerifier.create(statusDataMono)
                .expectNext(statusData)
                .verifyComplete();
    }

}
