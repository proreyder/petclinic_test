package com.tecsupt.petclinic.mapper;

import com.tecsupt.petclinic.domain.Owner;
import com.tecsupt.petclinic.dto.OwnerRequest;
import com.tecsupt.petclinic.dto.OwnerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OwnerMapper {
    @Mapping(target = "id", ignore = true)
    Owner toEntity(OwnerRequest request);
    OwnerResponse toResponse(Owner owner);
    @Mapping(target = "id", ignore = true)
    void update(OwnerRequest request, @MappingTarget Owner owner);
}
