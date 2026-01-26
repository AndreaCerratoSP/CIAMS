package com.spindox.ciams.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table (name = "software_license", schema = "inventory")
public class SoftwareLicense {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;
        @Column(name = "name")
        private String name;
        @Column(name = "expire_date")
        private Date expireDate;

        @ManyToMany(mappedBy = "softwareLicenses")
        private List<Asset> assets;
}
