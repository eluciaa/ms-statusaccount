package com.nttdata.bootcamp.ms.statusaccount.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitReportDto {

    private Customer consumer;
    private Debit debit;
    private List<Movement> movement;

}
