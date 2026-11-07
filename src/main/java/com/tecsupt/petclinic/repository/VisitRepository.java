package com.tecsupt.petclinic.repository;

import com.tecsupt.petclinic.domain.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface VisitRepository extends JpaRepository<Visit, Integer> {
}
