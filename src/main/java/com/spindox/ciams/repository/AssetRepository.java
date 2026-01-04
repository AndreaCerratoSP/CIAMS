package com.spindox.ciams.repository;

import com.spindox.ciams.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    public Optional<Asset> findAssetBySerialNumber(String serialNumber);

}
