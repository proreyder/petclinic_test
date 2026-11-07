package com.tecsupt.petclinic.exception;

public class VetNotFoundException extends RuntimeException {
    public VetNotFoundException(Integer id) {
        super("Veterinario no encontrado: " + id);
    }
}
