package com.tecsupt.petclinic.service;

import com.tecsupt.petclinic.dto.TypeRequest;
import com.tecsupt.petclinic.dto.TypeResponse;

public interface TypeService {
    TypeResponse create(TypeRequest request);
    TypeResponse findById(Integer id);
    TypeResponse update(Integer id, TypeRequest request);
    void delete(Integer id);
}
