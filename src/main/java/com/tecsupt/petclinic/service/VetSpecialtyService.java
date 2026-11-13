package com.tecsupt.petclinic.service;

import com.tecsupt.petclinic.dto.SpecialtyResponse;
import com.tecsupt.petclinic.dto.VetResponse;

import java.util.List;

public interface VetSpecialtyService {
    void assignSpecialtyToVet(Integer vetId, Integer specialtyId);
    List<SpecialtyResponse> findSpecialtiesByVet(Integer vetId);
    List<VetResponse> findVetsBySpecialty(Integer specialtyId);
    void delete(Integer vetId, Integer specialtyId);
}
