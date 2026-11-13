package com.tecsupt.petclinic.repository;

import com.tecsupt.petclinic.domain.VetSpecialty;
import com.tecsupt.petclinic.domain.VetSpecialtyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface VetSpecialtyRepository extends JpaRepository<VetSpecialty, VetSpecialtyId> {
    List<VetSpecialty> findByIdVetId(Integer vetId);
    List<VetSpecialty> findByIdSpecialtyId(Integer specialtyId);
}
