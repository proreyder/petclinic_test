package com.tecsup.petclinic.services;

import com.tecsupt.petclinic.dto.VetRequest;
import com.tecsupt.petclinic.dto.VetResponse;
import com.tecsupt.petclinic.exception.VetNotFoundException;
import com.tecsupt.petclinic.service.VetService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
@Transactional
public class VetServiceTest {
    @Autowired
    private VetService service;
    @Autowired
    private EntityManager entityManager;

    private VetRequest request() {
        return new VetRequest("Ana", "Torres", "ana@example.com", "999111222");
    }

    // Flush and clear ensure assertions read SQL data rather than the JPA cache.
    private void reload() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testCreateVet() {
        VetResponse created = service.create(request());
        assertNotNull(created.id());
        reload();
        VetResponse stored = service.findById(created.id());
        assertEquals(new VetResponse(created.id(), "Ana", "Torres",
                "ana@example.com", "999111222", true), stored);
    }

    @Test
    void testUpdateVet() {
        Integer id = service.create(request()).id();
        service.deactivate(id);
        service.update(id, new VetRequest("Luis", "Ramos", "luis@example.com", "888222333"));
        reload();
        assertEquals(new VetResponse(id, "Luis", "Ramos", "luis@example.com",
                "888222333", false), service.findById(id));
    }

    @Test
    void testFindVetById() {
        VetResponse vet = service.findById(1);
        assertEquals("James", vet.firstName());
        assertEquals("Carter", vet.lastName());
        assertTrue(vet.active());
    }

    @Test
    void testFindAll() {
        Integer id = service.create(request()).id();
        reload();
        assertTrue(service.findAll().stream().anyMatch(vet -> vet.id().equals(id)));
    }

    @Test
    void testDeleteVet() {
        Integer id = service.create(request()).id();
        service.delete(id);
        reload();
        assertThrows(VetNotFoundException.class, () -> service.findById(id));
    }

    @Test
    void testDeactivateVet() {
        Integer id = service.create(request()).id();
        service.deactivate(id);
        reload();
        assertFalse(service.findById(id).active());
    }

    @Test
    void testReactivateVet() {
        Integer id = service.create(request()).id();
        service.deactivate(id);
        reload();
        service.reactivate(id);
        reload();
        assertTrue(service.findById(id).active());
    }

    @Test
    void testFindActiveVets() {
        Integer activeId = service.create(request()).id();
        Integer inactiveId = service.create(request()).id();
        service.deactivate(inactiveId);
        reload();
        var vets = service.findActiveVets();
        assertTrue(vets.stream().anyMatch(vet -> vet.id().equals(activeId)));
        assertTrue(vets.stream().noneMatch(vet -> vet.id().equals(inactiveId)));
        assertTrue(vets.stream().allMatch(VetResponse::active));
    }

    @Test
    void testFindInactiveVets() {
        Integer activeId = service.create(request()).id();
        Integer inactiveId = service.create(request()).id();
        service.deactivate(inactiveId);
        reload();
        var vets = service.findInactiveVets();
        assertTrue(vets.stream().anyMatch(vet -> vet.id().equals(inactiveId)));
        assertTrue(vets.stream().noneMatch(vet -> vet.id().equals(activeId)));
        assertTrue(vets.stream().noneMatch(VetResponse::active));
    }

    @Test
    void testFindVetById_NotFound() {
        assertThrows(VetNotFoundException.class, () -> service.findById(Integer.MAX_VALUE));
    }

    @Test
    void testUpdateVet_NotFound() {
        assertThrows(VetNotFoundException.class, () -> service.update(Integer.MAX_VALUE, request()));
    }

    @Test
    void testDeleteVet_NotFound() {
        assertThrows(VetNotFoundException.class, () -> service.delete(Integer.MAX_VALUE));
    }

    @Test
    void testDeactivateVet_NotFound() {
        assertThrows(VetNotFoundException.class, () -> service.deactivate(Integer.MAX_VALUE));
    }

    @Test
    void testReactivateVet_NotFound() {
        assertThrows(VetNotFoundException.class, () -> service.reactivate(Integer.MAX_VALUE));
    }
}
