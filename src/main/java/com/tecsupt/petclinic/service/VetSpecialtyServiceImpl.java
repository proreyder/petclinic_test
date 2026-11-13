package com.tecsupt.petclinic.service;

import com.tecsupt.petclinic.domain.VetSpecialty;
import com.tecsupt.petclinic.domain.VetSpecialtyId;
import com.tecsupt.petclinic.dto.SpecialtyResponse;
import com.tecsupt.petclinic.dto.VetResponse;
import com.tecsupt.petclinic.exception.SpecialtyNotFoundException;
import com.tecsupt.petclinic.exception.VetNotFoundException;
import com.tecsupt.petclinic.mapper.SpecialtyMapper;
import com.tecsupt.petclinic.mapper.VetMapper;
import com.tecsupt.petclinic.repository.SpecialtyRepository;
import com.tecsupt.petclinic.repository.VetRepository;
import com.tecsupt.petclinic.repository.VetSpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VetSpecialtyServiceImpl implements VetSpecialtyService {
    private final VetSpecialtyRepository repository;
    private final VetRepository vetRepository;
    private final SpecialtyRepository specialtyRepository;
    private final VetMapper vetMapper;
    private final SpecialtyMapper specialtyMapper;

    @Override
    @Transactional
    public void assignSpecialtyToVet(Integer vetId, Integer specialtyId) {
        var vet = vetRepository.findById(vetId).orElseThrow(() -> new VetNotFoundException(vetId));
        var specialty = specialtyRepository.findById(specialtyId)
                .orElseThrow(() -> new SpecialtyNotFoundException(specialtyId));
        var id = new VetSpecialtyId(vetId, specialtyId);
        if (repository.existsById(id)) {
            throw new IllegalStateException("La especialidad ya esta asignada al veterinario");
        }
        repository.save(new VetSpecialty(vet, specialty));
    }

    @Override
    public List<SpecialtyResponse> findSpecialtiesByVet(Integer vetId) {
        return repository.findByIdVetId(vetId).stream()
                .map(relation -> specialtyMapper.toResponse(relation.getSpecialty())).toList();
    }

    @Override
    public List<VetResponse> findVetsBySpecialty(Integer specialtyId) {
        return repository.findByIdSpecialtyId(specialtyId).stream()
                .map(relation -> vetMapper.toResponse(relation.getVet())).toList();
    }

    @Override
    @Transactional
    public void delete(Integer vetId, Integer specialtyId) {
        repository.deleteById(new VetSpecialtyId(vetId, specialtyId));
    }
}
