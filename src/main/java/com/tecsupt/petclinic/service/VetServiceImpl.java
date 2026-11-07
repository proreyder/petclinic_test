package com.tecsupt.petclinic.service;

import com.tecsupt.petclinic.domain.Vet;
import com.tecsupt.petclinic.dto.VetRequest;
import com.tecsupt.petclinic.dto.VetResponse;
import com.tecsupt.petclinic.exception.VetNotFoundException;
import com.tecsupt.petclinic.mapper.VetMapper;
import com.tecsupt.petclinic.repository.VetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VetServiceImpl implements VetService {
    private final VetRepository repository;
    private final VetMapper mapper;

    @Override
    @Transactional
    public VetResponse create(VetRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    @Transactional
    public VetResponse update(Integer id, VetRequest request) {
        Vet vet = requireVet(id);
        mapper.update(request, vet);
        return mapper.toResponse(repository.save(vet));
    }

    @Override
    public VetResponse findById(Integer id) {
        return mapper.toResponse(requireVet(id));
    }

    @Override
    public List<VetResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        repository.delete(requireVet(id));
    }

    @Override
    @Transactional
    public VetResponse deactivate(Integer id) {
        return setActive(id, false);
    }

    @Override
    @Transactional
    public VetResponse reactivate(Integer id) {
        return setActive(id, true);
    }

    @Override
    public List<VetResponse> findActiveVets() {
        return findByActive(true);
    }

    @Override
    public List<VetResponse> findInactiveVets() {
        return findByActive(false);
    }

    private Vet requireVet(Integer id) {
        return repository.findById(id).orElseThrow(() -> new VetNotFoundException(id));
    }

    private VetResponse setActive(Integer id, boolean active) {
        Vet vet = requireVet(id);
        vet.setActive(active);
        return mapper.toResponse(repository.save(vet));
    }

    private List<VetResponse> findByActive(boolean active) {
        return repository.findByActive(active).stream().map(mapper::toResponse).toList();
    }
}
