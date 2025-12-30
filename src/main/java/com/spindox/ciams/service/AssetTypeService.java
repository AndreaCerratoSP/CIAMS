package com.spindox.ciams.service;

import com.spindox.ciams.model.AssetType;
import com.spindox.ciams.repository.AssetTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetTypeService {

    @Autowired

    private AssetTypeRepository assetTypeRepository;
    public List<AssetType> getAllAssetTypes() {
        return assetTypeRepository.findAll();
    }

    public AssetType getAssetTypeById(Long id) {
        return assetTypeRepository.findById(id).get();
    }

    public AssetType saveAssetType(AssetType assetType) {

        return assetTypeRepository.save(assetType);
    }


}
