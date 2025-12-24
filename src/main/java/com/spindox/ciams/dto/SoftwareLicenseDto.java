package com.spindox.ciams.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SoftwareLicenseDto {

    private Long id;
    private String name;
    private Date expireDate;

    private List<AssetDto> assets;
}
