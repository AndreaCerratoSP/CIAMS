package com.spindox.ciams.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AssetDto {

    private Long id;
    private String serialNumber;
    private Date acquisitionDate;
    private OfficeDto office;
    private AssetTypeDto assetType;

    private List<SoftwareLicenseDto> softwareLicenses;
}
