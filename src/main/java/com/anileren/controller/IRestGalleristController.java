package com.anileren.controller;

import com.anileren.dto.DtoGallerist;
import com.anileren.dto.DtoGalleristIU;

public interface IRestGalleristController {
    public RootEntity<DtoGallerist> saveGallerist(DtoGalleristIU input);
}
