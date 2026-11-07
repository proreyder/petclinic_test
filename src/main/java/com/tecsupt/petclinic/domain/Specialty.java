package com.tecsupt.petclinic.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "specialties")
@Getter
@Setter
@NoArgsConstructor
public class Specialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 80)
    private String name;
    @Column(length = 40)
    private String office;
    @Column(name = "h_open")
    private Integer hOpen;
    @Column(name = "h_close")
    private Integer hClose;
}
