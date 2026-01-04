package com.spindox.ciams.controller;

import com.spindox.ciams.dto.AssetTypeDto;
import com.spindox.ciams.service.AssetTypeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/AssetTypes")
public class AssetTypeController {

    @Autowired
    private AssetTypeService service;

    @GetMapping("/")
    public ResponseEntity<List<AssetTypeDto>> getAllAssetTypes(){

        return ResponseEntity.ok(service.getAllAssetTypes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetTypeDto> getAssetTypeById(@PathVariable long id) {

        System.out.println("Dentro controller id = " + id);
        try {
            return ResponseEntity.ok(service.getAssetTypeById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<AssetTypeDto>> getAssetTypeByName(@PathVariable String name) {

        return ResponseEntity.ok(service.getAssetTypeByName(name));
    }

    @PostMapping
    public ResponseEntity<AssetTypeDto> createAssetType(@RequestBody AssetTypeDto assetType) {
        if(AssetTypeIsNotValid(assetType)) {
            return  ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.saveAssetType(assetType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetTypeDto> updateAssetType(@RequestBody AssetTypeDto assetType, @PathVariable Long id) {
        if(AssetTypeIsNotValid(assetType)) {
            return  ResponseEntity.badRequest().build();
        }

        try{
            AssetTypeDto newAssetType = service.getAssetTypeById(id);
            newAssetType.setName(assetType.getName());
            newAssetType.setDescription(assetType.getDescription());
            return ResponseEntity.ok(service.saveAssetType(newAssetType));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AssetTypeDto> deleteAssetType(@PathVariable Long id) {
        try {
            service.deleteAssetType(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException | NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }


    private boolean AssetTypeIsNotValid(AssetTypeDto assetType) {
        if(assetType.getName() == null || assetType.getName().equals("")) {
            return true;
        }
        return false;
    }

}
