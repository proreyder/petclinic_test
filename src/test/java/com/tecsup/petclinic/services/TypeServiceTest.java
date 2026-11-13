package com.tecsup.petclinic.services;

import com.tecsupt.petclinic.dto.TypeRequest;
import com.tecsupt.petclinic.dto.TypeResponse;
import com.tecsupt.petclinic.exception.TypeNotFoundException;
import com.tecsupt.petclinic.service.TypeService;
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
public class TypeServiceTest {
    @Autowired
    private TypeService typeService;
    @Autowired
    private EntityManager entityManager;

    private TypeRequest request() {
        return new TypeRequest("Tipo caso 5", "Tipo temporal de prueba", true, "SMALL", 12, "LOW");
    }

    private TypeResponse expected(Integer id, TypeRequest request) {
        return new TypeResponse(id, request.name(), request.description(), request.active(),
                request.sizeCategory(), request.averageLifespan(), request.careLevel());
    }

    // Force SQL execution and read from the database instead of the JPA cache.
    private void reload() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testCreateType() {
        TypeRequest request = request();
        TypeResponse created = typeService.create(request);
        assertNotNull(created.id());
        reload();

        assertEquals(expected(created.id(), request), created);
        assertEquals(expected(created.id(), request), typeService.findById(created.id()));
    }

    @Test
    public void testFindTypeById() {
        TypeRequest request = request();
        Integer id = typeService.create(request).id();
        reload();

        assertEquals(expected(id, request), typeService.findById(id));
    }

    @Test
    public void testUpdateType() {
        Integer id = typeService.create(request()).id();
        reload();
        TypeRequest changes = new TypeRequest("Tipo actualizado caso 5", "Descripcion actualizada",
                false, "LARGE", 18, "HIGH");

        TypeResponse updated = typeService.update(id, changes);
        reload();

        assertEquals(expected(id, changes), updated);
        assertEquals(expected(id, changes), typeService.findById(id));
    }

    @Test
    public void testDeleteType() {
        // A new type has no pets referencing it, so deletion respects the foreign key.
        Integer id = typeService.create(request()).id();
        reload();
        assertNotNull(typeService.findById(id));

        typeService.delete(id);
        reload();

        assertThrows(TypeNotFoundException.class, () -> typeService.findById(id));
        Number remaining = (Number) entityManager.createNativeQuery("SELECT COUNT(*) FROM types WHERE id = :id")
                .setParameter("id", id).getSingleResult();
        assertEquals(0, remaining.intValue());
    }
}
