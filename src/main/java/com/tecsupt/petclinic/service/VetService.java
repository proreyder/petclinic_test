package com.tecsupt.petclinic.service;

import com.tecsupt.petclinic.dto.VetRequest;
import com.tecsupt.petclinic.dto.VetResponse;
import java.util.List;

public interface VetService {
    VetResponse create(VetRequest request);
    VetResponse update(Integer id, VetRequest request);
    VetResponse findById(Integer id);
    List<VetResponse> findAll();
    void delete(Integer id);
    VetResponse deactivate(Integer id);
    VetResponse reactivate(Integer id);
    List<VetResponse> findActiveVets();
    List<VetResponse> findInactiveVets();
}
