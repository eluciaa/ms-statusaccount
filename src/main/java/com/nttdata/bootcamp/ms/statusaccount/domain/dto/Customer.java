package com.nttdata.bootcamp.ms.statusaccount.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    private String id;
    private String dni;

    private String typeCustomer;

    private Boolean flagVip;

    private Boolean flagPyme;

    private String name;

    private String surName;


    private String address;

    private String status;

    private Date creationDate;

    private Date modificationDate;
}
