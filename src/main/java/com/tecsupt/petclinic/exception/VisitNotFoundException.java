package com.tecsupt.petclinic.exception;

public class VisitNotFoundException extends RuntimeException {
    public VisitNotFoundException(Integer id) {
        super("Visita no encontrada: " + id);
    }
}
