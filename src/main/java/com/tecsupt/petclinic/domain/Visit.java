package com.tecsupt.petclinic.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "visits")
@Getter
@Setter
@NoArgsConstructor
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "pet_id", nullable = false)
    private Integer petId;
    @Column(name = "vet_id")
    private Integer vetId;
    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;
    @Column(length = 255)
    private String description;
    @Column(precision = 10, scale = 2)
    private BigDecimal cost;
}
