package com.tecsupt.petclinic.service;

import com.tecsupt.petclinic.dto.VisitRequest;
import com.tecsupt.petclinic.dto.VisitResponse;

public interface VisitService {
    VisitResponse create(VisitRequest request);
    VisitResponse findById(Integer id);
    VisitResponse update(Integer id, VisitRequest request);
    void delete(Integer id);
}
