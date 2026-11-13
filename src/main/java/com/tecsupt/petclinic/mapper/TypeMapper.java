package com.tecsupt.petclinic.mapper;

import com.tecsupt.petclinic.domain.Type;
import com.tecsupt.petclinic.dto.TypeRequest;
import com.tecsupt.petclinic.dto.TypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TypeMapper {
    @Mapping(target = "id", ignore = true)
    Type toEntity(TypeRequest request);

    TypeResponse toResponse(Type type);

    @Mapping(target = "id", ignore = true)
    void update(TypeRequest request, @MappingTarget Type type);
}
