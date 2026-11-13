package com.tecsup.petclinic.services;

import com.tecsupt.petclinic.domain.Specialty;
import com.tecsupt.petclinic.domain.Vet;
import com.tecsupt.petclinic.domain.VetSpecialty;
import com.tecsupt.petclinic.domain.VetSpecialtyId;
import com.tecsupt.petclinic.dto.SpecialtyResponse;
import com.tecsupt.petclinic.dto.VetResponse;
import com.tecsupt.petclinic.service.VetSpecialtyService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
@Transactional
public class VetSpecialtyServiceTest {
    @Autowired
    private VetSpecialtyService service;
    @Autowired
    private EntityManager entityManager;

    private Integer vetId;
    private Integer specialtyId;

    @BeforeEach
    void setUp() {
        vetId = createVet("Ana");
        specialtyId = createSpecialty("Especialidad caso 6");
        reload();
    }

    private Integer createVet(String firstName) {
        Vet vet = new Vet();
        vet.setFirstName(firstName);
        vet.setLastName("Caso6");
        vet.setEmail("caso6@example.com");
        vet.setPhone("999111222");
        entityManager.persist(vet);
        return vet.getId();
    }

    private Integer createSpecialty(String name) {
        Specialty specialty = new Specialty();
        specialty.setName(name);
        specialty.setOffice("Consultorio caso 6");
        specialty.setHOpen(8);
        specialty.setHClose(16);
        entityManager.persist(specialty);
        return specialty.getId();
    }

    private void reload() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testAssignSpecialtyToVet() {
        service.assignSpecialtyToVet(vetId, specialtyId);
        reload();

        VetSpecialty relation = entityManager.find(VetSpecialty.class, new VetSpecialtyId(vetId, specialtyId));
        assertNotNull(relation);
        assertEquals(new VetSpecialtyId(vetId, specialtyId), relation.getId());
        assertEquals(vetId, relation.getVet().getId());
        assertEquals(specialtyId, relation.getSpecialty().getId());
        assertThrows(IllegalStateException.class, () -> service.assignSpecialtyToVet(vetId, specialtyId));
        Number count = (Number) entityManager.createNativeQuery(
                "SELECT COUNT(*) FROM vet_specialties WHERE vet_id = :vetId AND specialty_id = :specialtyId")
                .setParameter("vetId", vetId).setParameter("specialtyId", specialtyId).getSingleResult();
        assertEquals(1, count.intValue());
    }

    @Test
    public void testFindSpecialtiesByVet() {
        Integer otherVetId = createVet("Luis");
        Integer otherSpecialtyId = createSpecialty("Otra especialidad caso 6");
        service.assignSpecialtyToVet(vetId, specialtyId);
        service.assignSpecialtyToVet(otherVetId, otherSpecialtyId);
        reload();

        assertEquals(List.of(new SpecialtyResponse(specialtyId, "Especialidad caso 6",
                "Consultorio caso 6", 8, 16)), service.findSpecialtiesByVet(vetId));
    }

    @Test
    public void testFindVetsBySpecialty() {
        Integer otherVetId = createVet("Luis");
        Integer otherSpecialtyId = createSpecialty("Otra especialidad caso 6");
        service.assignSpecialtyToVet(vetId, specialtyId);
        service.assignSpecialtyToVet(otherVetId, otherSpecialtyId);
        reload();

        assertEquals(List.of(new VetResponse(vetId, "Ana", "Caso6", "caso6@example.com",
                "999111222", true)), service.findVetsBySpecialty(specialtyId));
    }

    @Test
    public void testDeleteVetSpecialty() {
        Integer otherSpecialtyId = createSpecialty("Especialidad conservada caso 6");
        service.assignSpecialtyToVet(vetId, specialtyId);
        service.assignSpecialtyToVet(vetId, otherSpecialtyId);
        reload();
        VetSpecialtyId id = new VetSpecialtyId(vetId, specialtyId);
        assertNotNull(entityManager.find(VetSpecialty.class, id));

        service.delete(vetId, specialtyId);
        reload();

        assertNull(entityManager.find(VetSpecialty.class, id));
        assertTrue(service.findVetsBySpecialty(specialtyId).isEmpty());
        assertNotNull(entityManager.find(Vet.class, vetId));
        assertNotNull(entityManager.find(Specialty.class, specialtyId));
        assertNotNull(entityManager.find(VetSpecialty.class, new VetSpecialtyId(vetId, otherSpecialtyId)));
    }
}
