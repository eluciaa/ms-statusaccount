package com.nttdata.bootcamp.ms.statusaccount.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
    private Integer accountId;
    private String accountNumber;
    private Float accountBalance;

    private Integer customerId;
    private String accountType;
}
