package com.nttdata.bootcamp.ms.statusaccount.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountMovementDto {

    private Customer consumer;
    private List<BankAccount> accounts;
    private List<Movement> movement;

}
