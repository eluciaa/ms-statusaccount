package com.nttdata.bootcamp.ms.statusaccount.application.controller;

import com.nttdata.bootcamp.ms.statusaccount.domain.dto.StatusAccountDto;
import com.nttdata.bootcamp.ms.statusaccount.domain.service.StatusAccountService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping(value = "/status")
public class StatusAccountController {

    @Autowired
    private StatusAccountService statusAccountService;

    /**
     * Método que devuelve todos los productos que tiene el cliente
     * @param idCustomer
     * @return
     */
    @CircuitBreaker(name = "allCB", fallbackMethod="fallBackGetAccount")
    @GetMapping("/{dni}/{idCustomer}")
    public Mono<StatusAccountDto> getStatusCustomer(@PathVariable String dni, @PathVariable Integer idCustomer){
        return statusAccountService.getAllProducts(dni, idCustomer);
    }


}
