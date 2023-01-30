package com.nttdata.bootcamp.ms.statusaccount.infrastructure.clients;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nttdata.bootcamp.ms.statusaccount.application.config.RestConfig;
import com.nttdata.bootcamp.ms.statusaccount.domain.dto.Customer;

import reactor.core.publisher.Flux;

@Service
public class CustomerRestClient {
    RestConfig restConfig = new RestConfig();
    public Flux<Customer> getCustomerById(String dni){
        return restConfig.getWebClient("http://localhost:8087")
                .build()
                .get()
                .uri("/customer/findByClient/" + dni)
                .retrieve()
                .bodyToFlux(Customer.class);
    }
	public Flux<Customer> getFindId(String dni) {
		WebClient webClient = WebClient.create("http://localhost:8087");

        return  webClient.get()
                .uri("/customer/findByDni/"+dni)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToFlux(Customer.class);
	}
}
