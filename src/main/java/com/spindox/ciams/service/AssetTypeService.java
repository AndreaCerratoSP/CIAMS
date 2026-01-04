package com.spindox.ciams.service;

import com.spindox.ciams.dto.AssetTypeDto;
import com.spindox.ciams.mapper.AssetTypeMapper;
import com.spindox.ciams.model.AssetType;
import com.spindox.ciams.repository.AssetTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetTypeService {

    @Autowired
    private AssetTypeRepository assetTypeRepository;

    @Autowired
    private AssetTypeMapper assetTypeMapper;

    public List<AssetTypeDto> getAllAssetTypes(){
        List<AssetType> assetTypes = assetTypeRepository.findAll();
        return assetTypeMapper.toDto(assetTypes);
    }

    public AssetTypeDto getAssetTypeById(Long id) throws EntityNotFoundException {

        System.out.println("Dentro service");
        Optional<AssetType> assetOpt =  assetTypeRepository.findById(id);
        if(assetOpt.isPresent()){
            AssetType assetType = assetOpt.get();
            System.out.println("Dentro service, trovato");
            return assetTypeMapper.toDto(assetType);
        }
        else{
            System.out.println("Dentro service, non trovato");
            throw  new EntityNotFoundException("Office with id " + id + " not found");
        }
    }

    public AssetTypeDto saveAssetType(AssetTypeDto assetTypeDto){
        AssetType assetType = assetTypeMapper.fromDto(assetTypeDto);
        assetTypeRepository.save(assetType);
        return assetTypeMapper.toDto(assetType);
    }

    public void deleteAssetType(Long id) throws EntityNotFoundException {
        AssetType assetType = assetTypeRepository.findById(id).get();
        assetTypeRepository.delete(assetType);
    }

    public AssetTypeDto getAssetTypeByName(String name) throws EntityNotFoundException {
        System.out.println("Dentro service");
        Optional<AssetType> assetOpt =  assetTypeRepository.findAssetTypeByName(name);
        if(assetOpt.isPresent()){
            AssetType assetType = assetOpt.get();
            System.out.println("Dentro service, trovato");
            return assetTypeMapper.toDto(assetType);
        }
        else{
            System.out.println("Dentro service, non trovato");
            throw  new EntityNotFoundException("Office with name " + name + " not found");
        }


    }

}
