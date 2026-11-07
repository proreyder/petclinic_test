package com.tecsupt.petclinic.service;

import com.tecsupt.petclinic.domain.Specialty;
import com.tecsupt.petclinic.dto.SpecialtyRequest;
import com.tecsupt.petclinic.dto.SpecialtyResponse;
import com.tecsupt.petclinic.exception.SpecialtyNotFoundException;
import com.tecsupt.petclinic.mapper.SpecialtyMapper;
import com.tecsupt.petclinic.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyRepository repository;
    private final SpecialtyMapper mapper;

    @Override
    @Transactional
    public SpecialtyResponse create(SpecialtyRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public SpecialtyResponse findById(Integer id) {
        return mapper.toResponse(requireSpecialty(id));
    }

    @Override
    @Transactional
    public SpecialtyResponse update(Integer id, SpecialtyRequest request) {
        Specialty specialty = requireSpecialty(id);
        mapper.update(request, specialty);
        return mapper.toResponse(repository.save(specialty));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        repository.delete(requireSpecialty(id));
    }

    private Specialty requireSpecialty(Integer id) {
        return repository.findById(id).orElseThrow(() -> new SpecialtyNotFoundException(id));
    }
}
