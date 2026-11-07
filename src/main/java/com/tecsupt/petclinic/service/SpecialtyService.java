package com.tecsupt.petclinic.service;

import com.tecsupt.petclinic.dto.SpecialtyRequest;
import com.tecsupt.petclinic.dto.SpecialtyResponse;

public interface SpecialtyService {
    SpecialtyResponse create(SpecialtyRequest request);
    SpecialtyResponse findById(Integer id);
    SpecialtyResponse update(Integer id, SpecialtyRequest request);
    void delete(Integer id);
}
