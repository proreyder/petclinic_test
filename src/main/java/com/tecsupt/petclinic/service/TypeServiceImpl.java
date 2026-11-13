package com.tecsupt.petclinic.service;

import com.tecsupt.petclinic.domain.Type;
import com.tecsupt.petclinic.dto.TypeRequest;
import com.tecsupt.petclinic.dto.TypeResponse;
import com.tecsupt.petclinic.exception.TypeNotFoundException;
import com.tecsupt.petclinic.mapper.TypeMapper;
import com.tecsupt.petclinic.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TypeServiceImpl implements TypeService {
    private final TypeRepository repository;
    private final TypeMapper mapper;

    @Override
    @Transactional
    public TypeResponse create(TypeRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public TypeResponse findById(Integer id) {
        return mapper.toResponse(requireType(id));
    }

    @Override
    @Transactional
    public TypeResponse update(Integer id, TypeRequest request) {
        Type type = requireType(id);
        mapper.update(request, type);
        return mapper.toResponse(repository.save(type));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        repository.delete(requireType(id));
    }

    private Type requireType(Integer id) {
        return repository.findById(id).orElseThrow(() -> new TypeNotFoundException(id));
    }
}
