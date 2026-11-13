package com.tecsupt.petclinic.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "vet_specialties")
@Getter
@NoArgsConstructor
public class VetSpecialty {
    @EmbeddedId
    private VetSpecialtyId id;

    @MapsId("vetId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vet_id")
    private Vet vet;

    @MapsId("specialtyId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "specialty_id")
    private Specialty specialty;

    @Column(name = "certification_date")
    private LocalDate certificationDate;
    @Column(name = "years_experience")
    private Integer yearsExperience = 0;
    @Column(name = "is_primary")
    private boolean primarySpecialty;
    @Column(length = 255)
    private String notes;

    public VetSpecialty(Vet vet, Specialty specialty) {
        this.id = new VetSpecialtyId(vet.getId(), specialty.getId());
        this.vet = vet;
        this.specialty = specialty;
    }
}
