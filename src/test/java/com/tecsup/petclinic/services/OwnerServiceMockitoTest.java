package com.tecsup.petclinic.services;

import com.tecsupt.petclinic.domain.Owner;
import com.tecsupt.petclinic.dto.OwnerRequest;
import com.tecsupt.petclinic.dto.OwnerResponse;
import com.tecsupt.petclinic.exception.OwnerNotFoundException;
import com.tecsupt.petclinic.mapper.OwnerMapper;
import com.tecsupt.petclinic.repository.OwnerRepository;
import com.tecsupt.petclinic.service.OwnerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerServiceMockitoTest {
    @Mock
    private OwnerRepository repository;
    private OwnerServiceImpl service;
    private final OwnerMapper mapper = Mappers.getMapper(OwnerMapper.class);
    private final OwnerRequest request = new OwnerRequest("Ana", "Torres", "Av. Lima 123", "Lima", "999111222");

    @BeforeEach
    void setUp() {
        service = new OwnerServiceImpl(repository, mapper);
    }

    private Owner owner() {
        Owner owner = mapper.toEntity(request);
        owner.setId(20);
        return owner;
    }

    private OwnerResponse expected() {
        return new OwnerResponse(20, "Ana", "Torres", "Av. Lima 123", "Lima", "999111222");
    }

    @Test
    void testCreateOwner() {
        when(repository.save(any(Owner.class))).thenAnswer(invocation -> {
            Owner owner = invocation.getArgument(0);
            assertNull(owner.getId());
            owner.setId(20);
            return owner;
        });
        assertEquals(expected(), service.create(request));
        verify(repository).save(any(Owner.class));
    }

    @Test
    void testUpdateOwner() {
        Owner existing = owner();
        when(repository.findById(20)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);
        var result = service.update(20, new OwnerRequest("Luis", "Ramos", "Calle Sol 456", "Cusco", "888222333"));
        assertEquals(new OwnerResponse(20, "Luis", "Ramos", "Calle Sol 456", "Cusco", "888222333"), result);
        verify(repository).save(existing);
    }

    @Test
    void testFindOwnerById() {
        when(repository.findById(20)).thenReturn(Optional.of(owner()));
        assertEquals(expected(), service.findById(20));
    }

    @Test
    void testDeleteOwner() {
        Owner existing = owner();
        when(repository.findById(20)).thenReturn(Optional.of(existing));
        service.delete(20);
        verify(repository).delete(existing);
    }

    @Test
    void testFindByLastName() {
        when(repository.findByLastName("Torres")).thenReturn(List.of(owner()));
        assertEquals(List.of(expected()), service.findByLastName("Torres"));
        verify(repository).findByLastName("Torres");
    }

    @Test
    void testFindByCity() {
        when(repository.findByCity("Lima")).thenReturn(List.of(owner()));
        assertEquals(List.of(expected()), service.findByCity("Lima"));
        verify(repository).findByCity("Lima");
    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(List.of(owner()));
        assertEquals(List.of(expected()), service.findAll());
    }

    @Test
    void testFindByLastName_Empty() {
        when(repository.findByLastName("Desconocido")).thenReturn(List.of());
        assertTrue(service.findByLastName("Desconocido").isEmpty());
    }

    @Test
    void testFindByCity_Empty() {
        when(repository.findByCity("Desconocida")).thenReturn(List.of());
        assertTrue(service.findByCity("Desconocida").isEmpty());
    }

    @Test
    void testFindOwnerById_NotFound() {
        when(repository.findById(20)).thenReturn(Optional.empty());
        assertThrows(OwnerNotFoundException.class, () -> service.findById(20));
    }

    @Test
    void testUpdateOwner_NotFound() {
        when(repository.findById(20)).thenReturn(Optional.empty());
        assertThrows(OwnerNotFoundException.class, () -> service.update(20, request));
        verify(repository, never()).save(any());
    }

    @Test
    void testDeleteOwner_NotFound() {
        when(repository.findById(20)).thenReturn(Optional.empty());
        assertThrows(OwnerNotFoundException.class, () -> service.delete(20));
        verify(repository, never()).delete(any());
    }
}
