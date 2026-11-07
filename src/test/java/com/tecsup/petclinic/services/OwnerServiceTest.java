package com.tecsup.petclinic.services;

import com.tecsupt.petclinic.dto.OwnerRequest;
import com.tecsupt.petclinic.dto.OwnerResponse;
import com.tecsupt.petclinic.exception.OwnerNotFoundException;
import com.tecsupt.petclinic.service.OwnerService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
@Transactional
class OwnerServiceTest {
    @Autowired
    private OwnerService service;
    @Autowired
    private EntityManager entityManager;

    private OwnerRequest request() {
        return new OwnerRequest("Ana", "Torres", "Av. Lima 123", "Lima", "999111222");
    }

    private void reload() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testCreateOwner() {
        Integer id = service.create(request()).id();
        assertNotNull(id);
        reload();
        assertEquals(new OwnerResponse(id, "Ana", "Torres", "Av. Lima 123", "Lima", "999111222"),
                service.findById(id));
    }

    @Test
    void testUpdateOwner() {
        Integer id = service.create(request()).id();
        service.update(id, new OwnerRequest("Luis", "Ramos", "Calle Sol 456", "Cusco", "888222333"));
        reload();
        assertEquals(new OwnerResponse(id, "Luis", "Ramos", "Calle Sol 456", "Cusco", "888222333"),
                service.findById(id));
    }

    @Test
    void testFindOwnerById() {
        assertEquals(new OwnerResponse(1, "George", "Franklin", "110 W. Liberty St.",
                "Madison", "6085551023"), service.findById(1));
    }

    @Test
    void testDeleteOwner() {
        Integer id = service.create(request()).id();
        service.delete(id);
        reload();
        assertThrows(OwnerNotFoundException.class, () -> service.findById(id));
    }

    @Test
    void testFindByLastName() {
        Integer first = service.create(request()).id();
        Integer second = service.create(new OwnerRequest("Luis", "Torres", "Otra calle", "Cusco", "123")).id();
        service.create(new OwnerRequest("Pedro", "Ramos", "Otra calle", "Lima", "456"));
        reload();
        var owners = service.findByLastName("Torres");
        assertEquals(Set.of(first, second), owners.stream().map(OwnerResponse::id).collect(Collectors.toSet()));
        assertTrue(owners.stream().allMatch(owner -> owner.lastName().equals("Torres")));
    }

    @Test
    void testFindByCity() {
        Integer first = service.create(request()).id();
        Integer second = service.create(new OwnerRequest("Luis", "Ramos", "Otra calle", "Lima", "123")).id();
        service.create(new OwnerRequest("Pedro", "Ramos", "Otra calle", "Cusco", "456"));
        reload();
        var owners = service.findByCity("Lima");
        assertEquals(Set.of(first, second), owners.stream().map(OwnerResponse::id).collect(Collectors.toSet()));
        assertTrue(owners.stream().allMatch(owner -> owner.city().equals("Lima")));
    }

    @Test
    void testFindAll() {
        Integer id = service.create(request()).id();
        reload();
        assertTrue(service.findAll().stream().anyMatch(owner -> owner.id().equals(id)));
    }

    @Test
    void testFindByLastName_Empty() {
        assertTrue(service.findByLastName("Apellido inexistente").isEmpty());
    }

    @Test
    void testFindByCity_Empty() {
        assertTrue(service.findByCity("Ciudad inexistente").isEmpty());
    }

    @Test
    void testFindOwnerById_NotFound() {
        assertThrows(OwnerNotFoundException.class, () -> service.findById(Integer.MAX_VALUE));
    }

    @Test
    void testUpdateOwner_NotFound() {
        assertThrows(OwnerNotFoundException.class, () -> service.update(Integer.MAX_VALUE, request()));
    }

    @Test
    void testDeleteOwner_NotFound() {
        assertThrows(OwnerNotFoundException.class, () -> service.delete(Integer.MAX_VALUE));
    }

    @Test
    void testDeleteOwnerCascadesToPetsAndVisits() {
        Integer ownerId = service.create(request()).id();
        entityManager.createNativeQuery("INSERT INTO pets (name, type_id, owner_id) VALUES ('Caso2', 1, :ownerId)")
                .setParameter("ownerId", ownerId).executeUpdate();
        Number petId = (Number) entityManager.createNativeQuery("SELECT id FROM pets WHERE owner_id = :ownerId")
                .setParameter("ownerId", ownerId).getSingleResult();
        entityManager.createNativeQuery("INSERT INTO visits (pet_id, visit_date) VALUES (:petId, CURRENT_DATE)")
                .setParameter("petId", petId.intValue()).executeUpdate();
        service.delete(ownerId);
        reload();
        Number pets = (Number) entityManager.createNativeQuery("SELECT COUNT(*) FROM pets WHERE owner_id = :ownerId")
                .setParameter("ownerId", ownerId).getSingleResult();
        Number visits = (Number) entityManager.createNativeQuery("SELECT COUNT(*) FROM visits WHERE pet_id = :petId")
                .setParameter("petId", petId.intValue()).getSingleResult();
        assertEquals(0, pets.intValue());
        assertEquals(0, visits.intValue());
        assertThrows(OwnerNotFoundException.class, () -> service.findById(ownerId));
    }
}
