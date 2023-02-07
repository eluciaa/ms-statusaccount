package com.nttdata.bootcamp.ms.statusaccount.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditPayConDto {

    private Customer consumer;
    private Credit credit;
    private String message;

}
