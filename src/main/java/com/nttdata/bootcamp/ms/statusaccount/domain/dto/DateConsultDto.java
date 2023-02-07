package com.nttdata.bootcamp.ms.statusaccount.domain.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateConsultDto {

    private Date dateStart;
    private Date dateEnd;

}
