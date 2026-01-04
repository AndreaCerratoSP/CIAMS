package com.spindox.ciams.repository;

import com.spindox.ciams.model.AssetType;
import com.spindox.ciams.model.Office;
import com.spindox.ciams.model.SoftwareLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SoftwareLicenseRepository extends JpaRepository<SoftwareLicense, Long> {

    public List<SoftwareLicense> findSoftwareLicenseByNameContainingOrderByName(String name);

    public List<SoftwareLicense> findSoftwareLicenseByExpireDateBetween(Timestamp start, Timestamp end);
}
