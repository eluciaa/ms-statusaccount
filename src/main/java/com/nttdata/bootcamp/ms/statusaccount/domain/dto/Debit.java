package com.nttdata.bootcamp.ms.statusaccount.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor
@AllArgsConstructor
public class Debit {

	private Integer id;
    private String cardNumber;
    private String expiryDate;
    private Float availableBalance;
    private List<Integer> accountId;
    private String customerId;
}
