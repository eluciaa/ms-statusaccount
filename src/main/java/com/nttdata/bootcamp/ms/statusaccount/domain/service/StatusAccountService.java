package com.nttdata.bootcamp.ms.statusaccount.domain.service;

import com.nttdata.bootcamp.ms.statusaccount.domain.dto.StatusAccountDto;
import reactor.core.publisher.Mono;

public interface StatusAccountService {

    Mono<StatusAccountDto> getAllProducts (String dni, Integer idCustomer);
}
