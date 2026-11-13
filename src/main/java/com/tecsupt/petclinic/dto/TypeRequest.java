package com.tecsupt.petclinic.dto;

public record TypeRequest(String name, String description, boolean active,
                          String sizeCategory, Integer averageLifespan, String careLevel) {
}
