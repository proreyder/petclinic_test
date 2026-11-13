package com.tecsupt.petclinic.dto;

public record TypeResponse(Integer id, String name, String description, boolean active,
                           String sizeCategory, Integer averageLifespan, String careLevel) {
}
