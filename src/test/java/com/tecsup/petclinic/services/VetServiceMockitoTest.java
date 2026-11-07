package com.tecsup.petclinic.services;

import com.tecsupt.petclinic.domain.Vet;
import com.tecsupt.petclinic.dto.VetRequest;
import com.tecsupt.petclinic.exception.VetNotFoundException;
import com.tecsupt.petclinic.mapper.VetMapper;
import com.tecsupt.petclinic.repository.VetRepository;
import com.tecsupt.petclinic.service.VetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VetServiceMockitoTest {
    @Mock
    private VetRepository repository;
    private VetServiceImpl service;
    private final VetRequest request = new VetRequest("Ana", "Torres", "ana@example.com", "999111222");

    @BeforeEach
    void setUp() {
        service = new VetServiceImpl(repository, Mappers.getMapper(VetMapper.class));
    }

    private Vet vet(boolean active) {
        Vet vet = new Vet();
        vet.setId(10);
        vet.setFirstName("Ana");
        vet.setLastName("Torres");
        vet.setEmail("ana@example.com");
        vet.setPhone("999111222");
        vet.setActive(active);
        return vet;
    }

    @Test
    void testCreateVet() {
        when(repository.save(any(Vet.class))).thenAnswer(invocation -> {
            Vet entity = invocation.getArgument(0);
            assertNull(entity.getId());
            assertTrue(entity.isActive());
            entity.setId(10);
            return entity;
        });
        var result = service.create(request);
        assertEquals(10, result.id());
        assertEquals(request.firstName(), result.firstName());
        assertEquals(request.lastName(), result.lastName());
        assertEquals(request.email(), result.email());
        assertEquals(request.phone(), result.phone());
        verify(repository).save(any(Vet.class));
    }

    @Test
    void testUpdateVet() {
        Vet existing = vet(false);
        when(repository.findById(10)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);
        var result = service.update(10, new VetRequest("Luis", "Ramos", "luis@example.com", "888222333"));
        assertEquals(10, result.id());
        assertEquals("Luis", result.firstName());
        assertEquals("Ramos", result.lastName());
        assertEquals("luis@example.com", result.email());
        assertEquals("888222333", result.phone());
        assertFalse(result.active());
        verify(repository).save(existing);
    }

    @Test
    void testFindVetById() {
        when(repository.findById(10)).thenReturn(Optional.of(vet(true)));
        assertEquals("Ana", service.findById(10).firstName());
    }

    @Test
    void testDeleteVet() {
        Vet existing = vet(true);
        when(repository.findById(10)).thenReturn(Optional.of(existing));
        service.delete(10);
        verify(repository).delete(existing);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testChangeActiveState(boolean active) {
        Vet existing = vet(!active);
        when(repository.findById(10)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);
        var result = active ? service.reactivate(10) : service.deactivate(10);
        assertEquals(active, result.active());
        verify(repository).save(existing);
        verify(repository, never()).delete(any());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testFindByActive(boolean active) {
        when(repository.findByActive(active)).thenReturn(List.of(vet(active)));
        var result = active ? service.findActiveVets() : service.findInactiveVets();
        assertEquals(1, result.size());
        assertEquals(10, result.getFirst().id());
        assertEquals(active, result.getFirst().active());
        verify(repository).findByActive(active);
    }

    @Test
    void testEmptyQueries() {
        when(repository.findAll()).thenReturn(List.of());
        when(repository.findByActive(true)).thenReturn(List.of());
        when(repository.findByActive(false)).thenReturn(List.of());
        assertTrue(service.findAll().isEmpty());
        assertTrue(service.findActiveVets().isEmpty());
        assertTrue(service.findInactiveVets().isEmpty());
    }

    @Test
    void testMissingVetNeverWrites() {
        when(repository.findById(10)).thenReturn(Optional.empty());
        assertThrows(VetNotFoundException.class, () -> service.findById(10));
        assertThrows(VetNotFoundException.class, () -> service.update(10, request));
        assertThrows(VetNotFoundException.class, () -> service.delete(10));
        assertThrows(VetNotFoundException.class, () -> service.deactivate(10));
        assertThrows(VetNotFoundException.class, () -> service.reactivate(10));
        verify(repository, never()).save(any());
        verify(repository, never()).delete(any());
    }
}
