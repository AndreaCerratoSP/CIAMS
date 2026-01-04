package com.spindox.ciams.mapper;

import com.spindox.ciams.dto.AssetDto;
import com.spindox.ciams.model.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper (componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class AssetMapper {

    public abstract AssetDto toDto(Asset asset);
    public abstract Asset fromDto(AssetDto assetDto);

    public abstract List<AssetDto> toDto(List<Asset> assets);
    public abstract List<Asset> fromDto(List<AssetDto> assets);

}
