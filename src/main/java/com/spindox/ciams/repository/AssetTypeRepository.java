package com.spindox.ciams.repository;

import com.spindox.ciams.model.AssetType;
import com.spindox.ciams.model.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetTypeRepository extends JpaRepository<AssetType, Long> {

}
