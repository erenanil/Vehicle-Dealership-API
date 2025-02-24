package com.anileren.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoGallerist extends DtoBase{

    private String firstName;


    private String lastName;


    private DtoAddress address;
}

