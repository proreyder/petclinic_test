package com.tecsupt.petclinic.service;

import com.tecsupt.petclinic.domain.Owner;
import com.tecsupt.petclinic.dto.OwnerRequest;
import com.tecsupt.petclinic.dto.OwnerResponse;
import com.tecsupt.petclinic.exception.OwnerNotFoundException;
import com.tecsupt.petclinic.mapper.OwnerMapper;
import com.tecsupt.petclinic.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository repository;
    private final OwnerMapper mapper;

    @Override
    @Transactional
    public OwnerResponse create(OwnerRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    @Transactional
    public OwnerResponse update(Integer id, OwnerRequest request) {
        Owner owner = requireOwner(id);
        mapper.update(request, owner);
        return mapper.toResponse(repository.save(owner));
    }

    @Override
    public OwnerResponse findById(Integer id) {
        return mapper.toResponse(requireOwner(id));
    }

    @Override
    public List<OwnerResponse> findAll() {
        return toResponses(repository.findAll());
    }

    @Override
    public List<OwnerResponse> findByLastName(String lastName) {
        return toResponses(repository.findByLastName(lastName));
    }

    @Override
    public List<OwnerResponse> findByCity(String city) {
        return toResponses(repository.findByCity(city));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        repository.delete(requireOwner(id));
    }

    private Owner requireOwner(Integer id) {
        return repository.findById(id).orElseThrow(() -> new OwnerNotFoundException(id));
    }

    private List<OwnerResponse> toResponses(List<Owner> owners) {
        return owners.stream().map(mapper::toResponse).toList();
    }
}
