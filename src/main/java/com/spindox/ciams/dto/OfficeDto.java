package com.spindox.ciams.dto;

import com.spindox.ciams.model.Asset;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class OfficeDto {

    private Long id;
    private String name;

    private List<AssetDto> assets;
}
