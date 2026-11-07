package com.tecsupt.petclinic.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VisitResponse(Integer id, Integer petId, Integer vetId, LocalDate visitDate,
                            String description, BigDecimal cost) {
}
