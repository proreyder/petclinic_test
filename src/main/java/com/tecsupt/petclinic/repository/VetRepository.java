package com.tecsupt.petclinic.repository;

import com.tecsupt.petclinic.domain.Vet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

@RepositoryRestResource(exported = false)
public interface VetRepository extends JpaRepository<Vet, Integer> {
    List<Vet> findByActive(boolean active);
}
