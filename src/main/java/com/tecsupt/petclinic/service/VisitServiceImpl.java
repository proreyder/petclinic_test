package com.tecsupt.petclinic.service;

import com.tecsupt.petclinic.domain.Visit;
import com.tecsupt.petclinic.dto.VisitRequest;
import com.tecsupt.petclinic.dto.VisitResponse;
import com.tecsupt.petclinic.exception.VisitNotFoundException;
import com.tecsupt.petclinic.mapper.VisitMapper;
import com.tecsupt.petclinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitServiceImpl implements VisitService {
    private final VisitRepository repository;
    private final VisitMapper mapper;

    @Override
    @Transactional
    public VisitResponse create(VisitRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public VisitResponse findById(Integer id) {
        return mapper.toResponse(requireVisit(id));
    }

    @Override
    @Transactional
    public VisitResponse update(Integer id, VisitRequest request) {
        Visit visit = requireVisit(id);
        mapper.update(request, visit);
        return mapper.toResponse(repository.save(visit));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        repository.delete(requireVisit(id));
    }

    private Visit requireVisit(Integer id) {
        return repository.findById(id).orElseThrow(() -> new VisitNotFoundException(id));
    }
}
