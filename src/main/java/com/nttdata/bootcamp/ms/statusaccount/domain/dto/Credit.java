package com.nttdata.bootcamp.ms.statusaccount.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credit {

    private Integer id;
    private String cardNumber;
    private Float creditLimit;
    private String expiryDate;

    private Float availableBalance;
    private String customerId;
    private String creditType;
}
