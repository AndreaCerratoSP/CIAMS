package com.spindox.ciams.repository;

import com.spindox.ciams.model.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetTypeRepository extends JpaRepository<AssetType, Long> {

    public List<AssetType> findAssetTypeByNameOrderByName(String name);
}
