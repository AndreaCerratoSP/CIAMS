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

    public List<AssetDto> getAllAssets() {
        List<Asset> assets = assetRepository.findAll();
        return assetMapper.toDto(assets);
    }

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

    public AssetDto saveAsset(AssetDto assetDto) {
        Asset asset = assetMapper.fromDto(assetDto);
        assetRepository.save(asset);
        return assetMapper.toDto(asset);
    }

    public void deleteAsset(Long id) {
        Asset asset = assetRepository.findById(id).get();
        assetRepository.delete(asset);
    }

    public AssetDto getAssetBySerialNumber(String serialNumber) {

        System.out.println("Dentro service");
        Optional<Asset> assetOpt =  assetRepository.findAssetBySerialNumber(serialNumber);
        if(assetOpt.isPresent()){
            Asset asset = assetOpt.get();
            System.out.println("Dentro service, trovato");
            return assetMapper.toDto(asset);
        }
        else{
            System.out.println("Dentro service, non trovato");
            throw  new EntityNotFoundException("Office with name " + serialNumber + " not found");
        }
    }
}