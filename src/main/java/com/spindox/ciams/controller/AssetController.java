package com.spindox.ciams.controller;

import com.spindox.ciams.dto.AssetDto;
import com.spindox.ciams.dto.OfficeDto;
import com.spindox.ciams.service.AssetService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/assets")
public class AssetController {

    @Autowired
    private AssetService service;

    @GetMapping("/{id}")
    public ResponseEntity<AssetDto> getAssetById(@PathVariable Long id) throws EntityNotFoundException {

        try {
            return ResponseEntity.ok(service.getAssetById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/serialn/{serialnumber}")
    public ResponseEntity<AssetDto> getAssetBySerialNumber(@PathVariable String serialnumber) throws EntityNotFoundException {
        try {
            return ResponseEntity.ok(service.getAssetBySerialNumber(serialnumber));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<AssetDto>> getAllAssets() throws EntityNotFoundException {
        return ResponseEntity.ok(service.getAllAssets());
    }

    @PostMapping("/")
    public ResponseEntity<AssetDto> createAsset(@RequestBody AssetDto assetDto) throws EntityNotFoundException {
        if(AssetIsNotValid(assetDto)) {
            return  ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.saveAsset(assetDto));
    }



    @PutMapping("/")
    public ResponseEntity<AssetDto> updateAsset(@RequestBody AssetDto assetDto, @PathVariable Long id) throws EntityNotFoundException {
        if(AssetIsNotValid(assetDto)) {
            return  ResponseEntity.badRequest().build();
        }

        try{
            AssetDto newAsset = service.getAssetById(id);
            newAsset.setSerialNumber(assetDto.getSerialNumber());

            return ResponseEntity.ok(service.saveAsset(newAsset));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<AssetDto> deleteAsset(@PathVariable Long id) throws EntityNotFoundException {
        try {
            service.deleteAsset(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException | NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }



    private boolean AssetIsNotValid(AssetDto asset) {
        if(asset.getSerialNumber() == null || asset.getSerialNumber().equals("")) {
            return true;
        }
        return false;
    }
}
