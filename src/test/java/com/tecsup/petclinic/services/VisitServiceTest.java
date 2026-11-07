package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Pet;
import com.tecsupt.petclinic.domain.Owner;
import com.tecsupt.petclinic.domain.Vet;
import com.tecsupt.petclinic.dto.VisitRequest;
import com.tecsupt.petclinic.dto.VisitResponse;
import com.tecsupt.petclinic.exception.VisitNotFoundException;
import com.tecsupt.petclinic.service.VisitService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
@Transactional
public class VisitServiceTest {
    @Autowired
    private VisitService visitService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Integer petId;
    private Integer vetId;

    @BeforeEach
    void setUp() {
        // The type has no entity yet. Obtain its generated key without relying on seed data.
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var statement = connection.prepareStatement(
                    "INSERT INTO types (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, "Tipo caso 4");
            return statement;
        }, keyHolder);
        int typeId = Objects.requireNonNull(keyHolder.getKey()).intValue();

        Owner owner = new Owner();
        owner.setFirstName("Dueño caso 4");
        owner.setLastName("Prueba");
        entityManager.persist(owner);

        Pet pet = new Pet();
        pet.setName("Mascota caso 4");
        pet.setTypeId(typeId);
        pet.setOwnerId(owner.getId());
        pet.setBirthDate(LocalDate.of(2022, 3, 10));
        entityManager.persist(pet);
        petId = pet.getId();

        Vet vet = new Vet();
        vet.setFirstName("Veterinario caso 4");
        vet.setLastName("Prueba");
        entityManager.persist(vet);
        vetId = vet.getId();
        reload();
    }

    private VisitRequest request() {
        return new VisitRequest(petId, vetId, LocalDate.of(2026, 6, 15),
                "Consulta de prueba caso 4", new BigDecimal("65.50"));
    }

    private VisitResponse expected(Integer id, VisitRequest request) {
        return new VisitResponse(id, request.petId(), request.vetId(), request.visitDate(),
                request.description(), request.cost());
    }

    private void reload() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testCreateVisit() {
        VisitRequest request = request();
        VisitResponse created = visitService.create(request);
        assertNotNull(created.id());
        reload();

        assertEquals(expected(created.id(), request), created);
        assertEquals(expected(created.id(), request), visitService.findById(created.id()));
    }

    @Test
    public void testFindVisitById() {
        VisitRequest request = request();
        Integer id = visitService.create(request).id();
        reload();

        assertEquals(expected(id, request), visitService.findById(id));
    }

    @Test
    public void testUpdateVisit() {
        Integer id = visitService.create(request()).id();
        reload();
        VisitRequest changes = new VisitRequest(petId, vetId, LocalDate.of(2026, 6, 20),
                "Control actualizado caso 4", new BigDecimal("120.75"));

        VisitResponse updated = visitService.update(id, changes);
        reload();

        assertEquals(expected(id, changes), updated);
        assertEquals(expected(id, changes), visitService.findById(id));
    }

    @Test
    public void testDeleteVisit() {
        Integer id = visitService.create(request()).id();
        reload();
        assertNotNull(visitService.findById(id));

        visitService.delete(id);
        reload();

        assertThrows(VisitNotFoundException.class, () -> visitService.findById(id));
        // Deleting a visit must preserve the pet and veterinarian created for this test.
        assertNotNull(entityManager.find(Pet.class, petId));
        assertNotNull(entityManager.find(Vet.class, vetId));
    }
}
