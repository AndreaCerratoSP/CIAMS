package com.spindox.ciams.controller;

import com.spindox.ciams.dto.AssetDto;
import com.spindox.ciams.dto.OfficeDto;
import com.spindox.ciams.dto.SoftwareLicenseDto;
import com.spindox.ciams.service.AssetService;
import com.spindox.ciams.service.AssetTypeService;
import com.spindox.ciams.service.OfficeService;
import com.spindox.ciams.service.SoftwareLicenceService;
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

    @Autowired
    private OfficeService officeService;

    @Autowired
    private AssetTypeService assetTypeService;

    @Autowired
    private SoftwareLicenceService licenseService;

    @GetMapping("/{id}")
    public ResponseEntity<AssetDto> getAssetById(@PathVariable Long id) throws EntityNotFoundException {

        try {
            return ResponseEntity.ok(service.getAssetById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/serialnumber/{serialnumber}")
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
        try{
            officeService.getOfficeById(assetDto.getOffice().getId());
            assetTypeService.getAssetTypeById(assetDto.getAssetType().getId());
        } catch(EntityNotFoundException e){
             return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(service.saveAsset(assetDto));
    }



    @PutMapping("/move")
    public ResponseEntity<AssetDto> moveAsset(@RequestParam Long assetId, @RequestParam Long officeId) throws EntityNotFoundException {
        AssetDto assetDto;
        OfficeDto officeDto;
        try {
            officeDto = officeService.getOfficeById(officeId);
            assetDto = service.getAssetById(assetId);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
        assetDto.setOffice(officeDto);

        return ResponseEntity.ok(service.saveAsset(assetDto));
    }

    @PutMapping("/install-software")
    public ResponseEntity<AssetDto> installSoftwareAsset(@RequestParam Long assetId, @RequestParam Long licenseId) throws EntityNotFoundException {
        AssetDto assetDto;
        SoftwareLicenseDto softwareLicenseDto;
        try {
            softwareLicenseDto = licenseService.getLicenseById(licenseId);
            assetDto = service.getAssetById(assetId);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }

        if (!assetDto.getSoftwareLicenses().contains(softwareLicenseDto)){
            assetDto.getSoftwareLicenses().add(softwareLicenseDto);
        }
        return ResponseEntity.ok(service.saveAsset(assetDto));
    }

    @PutMapping("/remove-software")
    public ResponseEntity<AssetDto> removeSoftwareAsset(@RequestParam Long assetId, @RequestParam Long licenseId) throws EntityNotFoundException {
        AssetDto assetDto;
        SoftwareLicenseDto softwareLicenseDto;
        try {
            softwareLicenseDto = licenseService.getLicenseById(licenseId);
            assetDto = service.getAssetById(assetId);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
        assetDto.getSoftwareLicenses().remove(softwareLicenseDto);
        return ResponseEntity.ok(service.saveAsset(assetDto));
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
        if(asset.getSerialNumber() == null || asset.getSerialNumber().equals("") ||
                asset.getAssetType() == null || asset.getAssetType().getId() == null ||
                asset.getOffice() == null || asset.getOffice().getId() == null) {
            return true;
        }
        return false;
    }
}
