package com.tecsupt.petclinic.service;

import com.tecsupt.petclinic.dto.OwnerRequest;
import com.tecsupt.petclinic.dto.OwnerResponse;
import java.util.List;

public interface OwnerService {
    OwnerResponse create(OwnerRequest request);
    OwnerResponse update(Integer id, OwnerRequest request);
    OwnerResponse findById(Integer id);
    List<OwnerResponse> findAll();
    List<OwnerResponse> findByLastName(String lastName);
    List<OwnerResponse> findByCity(String city);
    void delete(Integer id);
}
