package com.tecsupt.petclinic.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "types")
@Getter
@Setter
@NoArgsConstructor
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, length = 80)
    private String name;
    @Column(length = 100)
    private String description;
    private boolean active = true;
    @Column(name = "size_category", length = 20)
    private String sizeCategory;
    @Column(name = "average_lifespan")
    private Integer averageLifespan;
    @Column(name = "care_level", length = 20)
    private String careLevel;
}
