package com.nttdata.bootcamp.ms.statusaccount.domain.dto;

import lombok.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatusAccountDto {

    private Flux<Customer> consumer;
    private Flux<BankAccount> accounts;
    private Flux<Credit> credits;

}
