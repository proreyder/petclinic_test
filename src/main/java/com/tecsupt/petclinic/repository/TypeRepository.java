package com.tecsupt.petclinic.repository;

import com.tecsupt.petclinic.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface TypeRepository extends JpaRepository<Type, Integer> {
}
