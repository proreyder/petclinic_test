package com.tecsupt.petclinic.mapper;

import com.tecsupt.petclinic.domain.Visit;
import com.tecsupt.petclinic.dto.VisitRequest;
import com.tecsupt.petclinic.dto.VisitResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface VisitMapper {
    @Mapping(target = "id", ignore = true)
    Visit toEntity(VisitRequest request);

    VisitResponse toResponse(Visit visit);

    @Mapping(target = "id", ignore = true)
    void update(VisitRequest request, @MappingTarget Visit visit);
}
