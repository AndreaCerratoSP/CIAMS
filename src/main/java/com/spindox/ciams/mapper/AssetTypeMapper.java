package com.spindox.ciams.mapper;

import com.spindox.ciams.dto.AssetTypeDto;
import com.spindox.ciams.model.AssetType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper (componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class AssetTypeMapper {

    public abstract AssetTypeDto toDto(AssetType assetType);
    public abstract AssetType fromDto(AssetTypeDto assetType);

    public abstract List<AssetTypeDto> toDto(List<AssetType> assetTypes);
    public abstract List<AssetType> fromDto(List<AssetTypeDto> assetTypes);
}
