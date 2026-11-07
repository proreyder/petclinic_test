package com.tecsup.petclinic.services;

import com.tecsupt.petclinic.dto.SpecialtyRequest;
import com.tecsupt.petclinic.dto.SpecialtyResponse;
import com.tecsupt.petclinic.exception.SpecialtyNotFoundException;
import com.tecsupt.petclinic.service.SpecialtyService;
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
public class SpecialtyServiceTest {
    @Autowired
    private SpecialtyService specialtyService;

    @Autowired
    private EntityManager entityManager;

    private SpecialtyRequest newSpecialty() {
        return new SpecialtyRequest("Especialidad caso 3", "Consultorio prueba", 8, 16);
    }

    // Read persisted data rather than the first-level JPA cache.
    private void reload() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testCreateSpeciality() {
        SpecialtyRequest request = newSpecialty();
        SpecialtyResponse created = specialtyService.create(request);

        assertNotNull(created.id());
        reload();
        SpecialtyResponse stored = specialtyService.findById(created.id());
        assertEquals(new SpecialtyResponse(created.id(), request.name(), request.office(),
                request.hOpen(), request.hClose()), stored);
    }

    @Test
    public void testFindSpecialityById() {
        SpecialtyResponse created = specialtyService.create(newSpecialty());
        reload();

        SpecialtyResponse found = specialtyService.findById(created.id());

        assertEquals(created, found);
    }

    @Test
    public void testUpdateSpeciality() {
        Integer id = specialtyService.create(newSpecialty()).id();
        reload();
        SpecialtyRequest changes = new SpecialtyRequest("Especialidad actualizada", "Consultorio nuevo", 10, 19);

        SpecialtyResponse updated = specialtyService.update(id, changes);
        reload();
        SpecialtyResponse stored = specialtyService.findById(id);

        assertEquals(new SpecialtyResponse(id, changes.name(), changes.office(),
                changes.hOpen(), changes.hClose()), updated);
        assertEquals(updated, stored);
    }

    @Test
    public void testDeleteSpeciality() {
        Integer id = specialtyService.create(newSpecialty()).id();
        reload();
        assertNotNull(specialtyService.findById(id));

        specialtyService.delete(id);
        reload();

        assertThrows(SpecialtyNotFoundException.class, () -> specialtyService.findById(id));
    }
}
