package com.anileren.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoSaledCar extends DtoBase{

    private DtoGallerist gallerist;


    private DtoCar car;


    private DtoCustomer customer;

}
