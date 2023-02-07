package com.nttdata.bootcamp.ms.statusaccount.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountReportDto {

    private Customer consumer;
    private BankAccount account;
    private List<Movement> movement;

}
