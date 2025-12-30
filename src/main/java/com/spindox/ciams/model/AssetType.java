package com.spindox.ciams.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "asset_type", schema = "inventory")
public class AssetType {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;
        @Column (name = "name")
        private String name;
        @Column (name = "description")
        private String description;

        @OneToMany(mappedBy = "assetType")
        private List<Asset> assets;
}
