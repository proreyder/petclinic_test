package com.tecsupt.petclinic.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class VetSpecialtyId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "vet_id")
    private Integer vetId;
    @Column(name = "specialty_id")
    private Integer specialtyId;
}
