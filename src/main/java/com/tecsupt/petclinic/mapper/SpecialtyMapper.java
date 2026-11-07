package com.tecsupt.petclinic.mapper;

import com.tecsupt.petclinic.domain.Specialty;
import com.tecsupt.petclinic.dto.SpecialtyRequest;
import com.tecsupt.petclinic.dto.SpecialtyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SpecialtyMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "HOpen", source = "hOpen")
    @Mapping(target = "HClose", source = "hClose")
    Specialty toEntity(SpecialtyRequest request);

    @Mapping(target = "hOpen", source = "HOpen")
    @Mapping(target = "hClose", source = "HClose")
    SpecialtyResponse toResponse(Specialty specialty);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "HOpen", source = "hOpen")
    @Mapping(target = "HClose", source = "hClose")
    void update(SpecialtyRequest request, @MappingTarget Specialty specialty);
}
