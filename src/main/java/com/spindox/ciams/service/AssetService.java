package com.spindox.ciams.service;

import com.spindox.ciams.dto.AssetDto;
import com.spindox.ciams.mapper.AssetMapper;
import com.spindox.ciams.model.Asset;
import com.spindox.ciams.repository.AssetRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
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
        log.info("Inside the service, getAllAssets method");
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

        log.info("Inside the service, getAssetById method");
        Optional<Asset> assetOpt =  assetRepository.findById(id);
        if(assetOpt.isPresent()){
            Asset asset = assetOpt.get();
            log.info("Asset found with id {}", id);
            return assetMapper.toDto(asset);
        }
        else{
            log.info("Asset not found with id {}", id);
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
        log.info("Asset saved with id {}", asset.getId());
        return assetMapper.toDto(asset);
    }

    /**
     * Deletes an asset on the database
     *
     * @param id the pk of the asset to cancel
     */
    public void deleteAsset(Long id) {
        Optional<Asset> assetOpt =  assetRepository.findById(id);
        if(assetOpt.isPresent()){
            Asset asset = assetOpt.get();
            log.info("Asset deleted with id {}", asset.getId());
            assetRepository.delete(asset);
        }
        else{
            log.error("Asset not found with id {}", id);
            throw  new EntityNotFoundException("Asset with id " + id + " not found");
        }
    }

    /**
     * Finds an asset by his serial number
     *
     * @param serialNumber of the asset
     * @return the found asset
     * @throws EntityNotFoundException when the asset doesn't get found
     */
    public AssetDto getAssetBySerialNumber(String serialNumber) throws EntityNotFoundException{

        log.info("Inside the service, getAssetBySerialNumber method");
        Optional<Asset> assetOpt =  assetRepository.findAssetBySerialNumber(serialNumber);
        if(assetOpt.isPresent()){
            Asset asset = assetOpt.get();
            log.info("Asset found with id {}", asset.getId());
            return assetMapper.toDto(asset);
        }
        else{
            log.info("Asset not found with id {}", serialNumber);
            throw  new EntityNotFoundException("Office with name " + serialNumber + " not found");
        }
    }
}