package com.anileren.service;

import com.anileren.dto.DtoGallerist;
import com.anileren.dto.DtoGalleristIU;

public interface IGalleristService {
    public DtoGallerist saveGallerist(DtoGalleristIU input);
}
