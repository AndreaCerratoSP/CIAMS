package com.spindox.ciams.dto;

import com.spindox.ciams.model.Asset;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class AssetTypeDto {

    private Long id;
    private String name;
    private String description;

    //private List<AssetDto> assets;
}
