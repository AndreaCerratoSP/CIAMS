package com.spindox.ciams.service;

import com.spindox.ciams.dto.AssetDto;
import com.spindox.ciams.mapper.AssetMapper;
import com.spindox.ciams.model.Asset;
import com.spindox.ciams.repository.AssetRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private AssetMapper assetMapper;

    /**
     * Finds all the assets
     *
     * @return a list of all the assets
     */
    public List<AssetDto> getAllAssets() {
        List<Asset> assets = assetRepository.findAll();
        return assetMapper.toDto(assets);
    }

    /**
     * Finds an asset by his id
     *
     * @param id the pk of the asset
     * @return the found asset
     * @throws EntityNotFoundException when the asset is not found
     */
    public AssetDto getAssetById(Long id) throws EntityNotFoundException {

        System.out.println("Dentro service");
        Optional<Asset> assetOpt =  assetRepository.findById(id);
        if(assetOpt.isPresent()){
            Asset asset = assetOpt.get();
            System.out.println("Dentro service, trovato");
            return assetMapper.toDto(asset);
        }
        else{
            System.out.println("Dentro service, non trovato");
            throw  new EntityNotFoundException("Office with id " + id + " not found");
        }

    }

    /**
     * Saves an asset on the database
     *
     * @param assetDto is the asset to save
     * @return the saved asset
     */
    public AssetDto saveAsset(AssetDto assetDto) {
        Asset asset = assetMapper.fromDto(assetDto);
        assetRepository.save(asset);
        return assetMapper.toDto(asset);
    }

    /**
     * Deletes an asset on the database
     *
     * @param id the pk of the asset to cancel
     */
    public void deleteAsset(Long id) {
        Asset asset = assetRepository.findById(id).get();
        assetRepository.delete(asset);
    }

    /**
     * Finds an asset by his serial number
     *
     * @param serialNumber of the asset
     * @return the found asset
     * @throws EntityNotFoundException when the asset doesn't get found
     */
    public AssetDto getAssetBySerialNumber(String serialNumber) throws EntityNotFoundException{

        Optional<Asset> assetOpt =  assetRepository.findAssetBySerialNumber(serialNumber);
        if(assetOpt.isPresent()){
            Asset asset = assetOpt.get();
            return assetMapper.toDto(asset);
        }
        else{
            throw  new EntityNotFoundException("Office with name " + serialNumber + " not found");
        }
    }
}