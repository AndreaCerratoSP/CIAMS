package com.spindox.ciams.controller;

import com.spindox.ciams.dto.AssetDto;
import com.spindox.ciams.dto.OfficeDto;
import com.spindox.ciams.dto.SoftwareLicenseDto;
import com.spindox.ciams.service.AssetService;
import com.spindox.ciams.service.AssetTypeService;
import com.spindox.ciams.service.OfficeService;
import com.spindox.ciams.service.SoftwareLicenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

    /**
     * Retrieves a single asset by ID.
     *
     * @param id the unique identifier of the asset to retrieve
     * @return 200 with the AssetDto on success,
     *         404 if the asset does not exist,
     *         401 if unauthorized
     * @throws EntityNotFoundException if the asset is not found
     */
    @Operation(
            summary = "Get an asset by ID",
            description = "Fetches the asset identified by the given ID and returns the resource.",
            security = { @SecurityRequirement(name = "basicAuth") },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Asset successfully retrieved",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AssetDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Asset not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AssetDto> getAssetById(@PathVariable Long id) throws EntityNotFoundException {

        try {
            return ResponseEntity.ok(service.getAssetById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves a single asset by serial number.
     *
     * @param serialnumber the serial number of the asset to retrieve
     * @return 200 with the AssetDto on success,
     *         400 if the serial number is invalid,
     *         404 if the asset does not exist,
     *         401 if unauthorized
     * @throws EntityNotFoundException if the asset is not found
     */
    @Operation(
            summary = "Get an asset by serial number",
            description = "Fetches the asset identified by the given serial number and returns the resource.",
            security = { @SecurityRequirement(name = "basicAuth") },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Asset successfully retrieved",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AssetDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid serial number",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Asset not found",
                            content = @Content
                    )
            }
    )

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

    @DeleteMapping("/{id}")
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
