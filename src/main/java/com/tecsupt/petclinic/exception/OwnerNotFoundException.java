package com.tecsupt.petclinic.exception;

public class OwnerNotFoundException extends RuntimeException {
    public OwnerNotFoundException(Integer id) {
        super("Dueño no encontrado: " + id);
    }
}
