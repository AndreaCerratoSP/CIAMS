package com.spindox.ciams.mapper;

import com.spindox.ciams.dto.OfficeDto;
import com.spindox.ciams.model.Office;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper (componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class OfficeMapper {

    public abstract OfficeDto toDto(Office office);
    public abstract Office fromDto(OfficeDto dto);

    public abstract List<OfficeDto> toDto(List<Office> offices);
    public abstract List<Office> fromDto(List<OfficeDto> dtos);

}
