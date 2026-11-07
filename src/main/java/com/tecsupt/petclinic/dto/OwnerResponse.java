package com.tecsupt.petclinic.dto;

public record OwnerResponse(Integer id, String firstName, String lastName, String address,
                            String city, String telephone) {
}
