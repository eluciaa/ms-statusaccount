package com.nttdata.bootcamp.ms.statusaccount.domain.dto;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;

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
    private Float amount;
    private TypeCurrency currency;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @CreatedDate
    private Date transactionDate;
    private String productType; // AHORRO, C_CORRIENTE, PLAZO_FIJO, CRE_PERSONAL, CRED_EMPRESARIAL, TAR_CRED_PERSONAL, TAR_CRED_EMPRESARIAL, DEBITO"
    private Integer accountId;
    private String customerId;
    private String transactionType;  // DEPOSITO, RETIRO, PAGO, CONSUMO  

}
