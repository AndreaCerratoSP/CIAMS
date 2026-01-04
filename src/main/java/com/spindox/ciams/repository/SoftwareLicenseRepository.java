package com.spindox.ciams.repository;

import com.spindox.ciams.model.AssetType;
import com.spindox.ciams.model.Office;
import com.spindox.ciams.model.SoftwareLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SoftwareLicenseRepository extends JpaRepository<SoftwareLicense, Long> {

    public Optional<SoftwareLicense> findSoftwareLicenseByName(String name);
}
