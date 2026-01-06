package com.spindox.ciams.service;

import com.spindox.ciams.dto.AssetTypeDto;
import com.spindox.ciams.mapper.AssetTypeMapper;
import com.spindox.ciams.model.AssetType;
import com.spindox.ciams.repository.AssetTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AssetTypeService {

    @Autowired
    private AssetTypeRepository assetTypeRepository;

    @Autowired
    private AssetTypeMapper assetTypeMapper;

    /**
     * Finds all the AssetTypes
     *
     * @return a list of all the asset types
     */
    public List<AssetTypeDto> getAllAssetTypes(){

        log.info("Inside the service, getAllAssetTypes method");
        List<AssetType> assetTypes = assetTypeRepository.findAll();
        return assetTypeMapper.toDto(assetTypes);
    }

    /**
     * Finds an Asset Type by his id
     *
     * @param id of the office to search
     * @return the found asset type
     * @throws EntityNotFoundException when the asset type is not found
     */
    public AssetTypeDto getAssetTypeById(Long id) throws EntityNotFoundException {

        log.info("Inside the service, getAssetTypeById method");
        Optional<AssetType> assetOpt =  assetTypeRepository.findById(id);
        if(assetOpt.isPresent()){
            AssetType assetType = assetOpt.get();
            log.info("assetType found with id {}", id);
            return assetTypeMapper.toDto(assetType);
        }
        else{
            log.info("assetType not found with id {}", id);
            throw  new EntityNotFoundException("Office with id " + id + " not found");
        }
    }

    /**
     * Saves an asset type on the database
     *
     * @param assetTypeDto is the asset type to save
     * @return the asset type saved
     */
    public AssetTypeDto saveAssetType(AssetTypeDto assetTypeDto){
        AssetType assetType = assetTypeMapper.fromDto(assetTypeDto);
        assetTypeRepository.save(assetType);
        log.info("assetType saved with id {}", assetType.getId());
        return assetTypeMapper.toDto(assetType);
    }

    /**
     * Deletes an asset type
     *
     * @param id the pk of the asset type to cancel
     * @throws EntityNotFoundException when the asset type is not found
     */
    public void deleteAssetType(Long id) throws EntityNotFoundException {
        AssetType assetType = assetTypeRepository.findById(id).get();
        assetTypeRepository.delete(assetType);
        log.info("assetType deleted with id {}", id);
    }

    /**
     * Finds an asset type by his name
     *
     * @param name is then name of the asset type to find
     * @return the asset type found
     * @throws EntityNotFoundException when the asset type is not found
     */
    public List<AssetTypeDto> getAssetTypeByName(String name) throws EntityNotFoundException {

        List<AssetType> assetTypeList =  assetTypeRepository.findAssetTypeByNameOrderByName(name);
        log.info("assetTypeList found with name {}", name);
        return assetTypeMapper.toDto(assetTypeList);
    }

}
