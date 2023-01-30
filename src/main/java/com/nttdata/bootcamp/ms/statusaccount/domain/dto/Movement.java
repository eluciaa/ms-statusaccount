package com.nttdata.bootcamp.ms.statusaccount.domain.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nttdata.bootcamp.ms.statusaccount.enums.TypeCurrency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movement {

    private String id;
    private String accountNumber;

    private BigDecimal amount;

    private String dni;

    private TypeCurrency currency;

    private Integer cvc;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @CreatedDate
    private Date creationDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @LastModifiedDate
    private Date modificationDate;

}
