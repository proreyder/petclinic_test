package com.tecsupt.petclinic.exception;

public class SpecialtyNotFoundException extends RuntimeException {
    public SpecialtyNotFoundException(Integer id) {
        super("Especialidad no encontrada: " + id);
    }
}
