package com.tecsupt.petclinic.dto;

public record VetResponse(Integer id, String firstName, String lastName,
                          String email, String phone, boolean active) {
}
