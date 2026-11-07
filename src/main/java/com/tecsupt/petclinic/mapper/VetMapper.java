package com.tecsupt.petclinic.mapper;

import com.tecsupt.petclinic.domain.Vet;
import com.tecsupt.petclinic.dto.VetRequest;
import com.tecsupt.petclinic.dto.VetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VetMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    Vet toEntity(VetRequest request);

    VetResponse toResponse(Vet vet);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    void update(VetRequest request, @MappingTarget Vet vet);
}
