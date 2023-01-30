package com.nttdata.bootcamp.ms.statusaccount.rest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.nttdata.bootcamp.ms.statusaccount.application.controller.StatusAccountController;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.AccountMovementDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.BankAccount;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Credit;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.CreditMovementDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Customer;
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

//	@BeforeAll
//    public static void setUp() {
//		webClient = WebTestClient.bindToController(new StatusAccountController(service))
//				.configureClient()
//				.baseUrl("/status/products/12345678/2")
//				.build();
//    }

	/*return all products of a customer*/
	@Test
	public void getAllClientTests( ) {
		List<BankAccount> account = Arrays.asList(new BankAccount(3,"0011-1235-3453-1245",5000.0f,1,"AHORRO"));
		List<Credit> credit = Arrays.asList(new Credit(2, "0112-1235-3453-0002", 5000.0f, "10",10.0f));
		Customer customer = new Customer("63d17a46f4a3a745571eef09", "12345678", "PERSONAL", false, false, "Alex", "Apellido", "direccion", "ACTIVE", null, null);
		StatusAccountDto statusData = new StatusAccountDto(customer, account, credit);
		when(service.getAllProducts("12345678", 2))
			.thenReturn(Mono.just(statusData));
		
		webClient = WebTestClient.bindToController(new StatusAccountController(service))
				.configureClient()
				.baseUrl("/status/products/12345678/2")
				.build();

		Mono<StatusAccountDto> responseBody = webClient.get()
			.accept(MediaType.APPLICATION_NDJSON)
			.exchange()
			.expectStatus().isOk()
			.returnResult(StatusAccountDto.class)
			.getResponseBody().elementAt(0);

		StepVerifier.create(responseBody)
			.expectNext(statusData)
			.verifyComplete();
	}
	
	/*return account product data*/
	@Test
	public void getProductIdAccountTests( ) {
		List<BankAccount> account = Arrays.asList(new BankAccount(3,"0011-1235-3453-1245",5000.0f,1,"AHORRO"));
		List<Movement> movement = Arrays.asList(new Movement("1", null, null, null, null, null, null, null));
		Customer customer = new Customer("63d17a46f4a3a745571eef09", "12345678", "PERSONAL", false, false, "Alex", "Apellido", "direccion", "ACTIVE", null, null);
		AccountMovementDto statusData = new AccountMovementDto(customer, account, movement);
		when(service.getProductIdAccount("12345678", 2))
			.thenReturn(Mono.just(statusData));
		
		webClient = WebTestClient.bindToController(new StatusAccountController(service))
				.configureClient()
				.baseUrl("/status/accountproduct/12345678/2")
				.build();

		Mono<AccountMovementDto> responseBody = webClient.get()
			.accept(MediaType.APPLICATION_NDJSON)
			.exchange()
			.expectStatus().isOk()
			.returnResult(AccountMovementDto.class)
			.getResponseBody().elementAt(0);

		StepVerifier.create(responseBody)
			.expectNext(statusData)
			.verifyComplete();
	}
	
	/*return credit product data*/
	@Test
	public void getProductIdCreditTests( ) {
		List<Credit> credit = Arrays.asList(new Credit(2, "0112-1235-3453-0002", 5000.0f, "10",10.0f));
		List<Movement> movement = Arrays.asList(new Movement("1", null, null, null, null, null, null, null));
		Customer customer = new Customer("63d17a46f4a3a745571eef09", "12345678", "PERSONAL", false, false, "Alex", "Apellido", "direccion", "ACTIVE", null, null);
		CreditMovementDto statusData = new CreditMovementDto(customer, credit, movement);
		when(service.getProductIdCredit("12345678", 2))
			.thenReturn(Mono.just(statusData));
		
		webClient = WebTestClient.bindToController(new StatusAccountController(service))
				.configureClient()
				.baseUrl("/status/creditproduct/12345678/2")
				.build();

		Mono<CreditMovementDto> responseBody = webClient.get()
			.accept(MediaType.APPLICATION_NDJSON)
			.exchange()
			.expectStatus().isOk()
			.returnResult(CreditMovementDto.class)
			.getResponseBody().elementAt(0);

		StepVerifier.create(responseBody)
			.expectNext(statusData)
			.verifyComplete();
	}

}
