package com.spindox.ciams.mapper;

import com.spindox.ciams.dto.SoftwareLicenseDto;
import com.spindox.ciams.model.SoftwareLicense;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper (componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class SoftwareLicenseMapper {

    public abstract SoftwareLicenseDto toDto(SoftwareLicense softwareLicense);
    public abstract SoftwareLicense fromDto(SoftwareLicenseDto softwareLicenseDto);

    public abstract List<SoftwareLicenseDto> toDto(List<SoftwareLicense> softwareLicense);
    public abstract List<SoftwareLicense> fromDto(List<SoftwareLicenseDto> softwareLicense);

}
