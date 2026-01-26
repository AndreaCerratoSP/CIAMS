package com.spindox.ciams.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "asset", schema = "inventory")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id;
    @Column (name = "serial_number")
    private String serialNumber;
    @Column (name = "acquisition_date")
    private Date acquisitionDate;

    @ManyToOne
    @JoinColumn(name = "office_id")
    private Office office;

    @ManyToOne
    @JoinColumn(name = "asset_type_id")
    private AssetType assetType;


    @ManyToMany
    @JoinTable(
            name = "asset_licence",
            schema = "inventory",
            joinColumns = @JoinColumn(name = "asset_id"),
            inverseJoinColumns = @JoinColumn(name = "licence_id")
    )
    private List<SoftwareLicense> softwareLicenses;

}
