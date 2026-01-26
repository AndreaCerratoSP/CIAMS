package com.spindox.ciams.controller;

import com.spindox.ciams.dto.AssetDto;
import com.spindox.ciams.dto.OfficeDto;
import com.spindox.ciams.dto.SoftwareLicenseDto;
import com.spindox.ciams.service.AssetService;
import com.spindox.ciams.service.AssetTypeService;
import com.spindox.ciams.service.OfficeService;
import com.spindox.ciams.service.SoftwareLicenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
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
        log.info("get an asset by ID {}", id);
        return ResponseEntity.ok(service.getAssetById(id));

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

        log.info("get an asset by serial number {}", serialnumber);
        return ResponseEntity.ok(service.getAssetBySerialNumber(serialnumber));

    }

    /**
     * Retrieves all assets.
     *
     * @return 200 with the list of AssetDto on success
     * @throws EntityNotFoundException (not typically thrown for list endpoints; consider returning 204 instead)
     */
    @Operation(
            summary = "Get all assets",
            description = "Fetches all assets and returns them as a list.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Assets successfully retrieved",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssetDto.class))
                            )
                    )
            }
    )
    @GetMapping("/")
    public ResponseEntity<List<AssetDto>> getAllAssets() throws EntityNotFoundException {
        log.info("get all assets");
        return ResponseEntity.ok(service.getAllAssets());
    }


    /**
     * Creates a new asset.
     *
     * @param assetDto the AssetDto containing the asset details to create
     * @return 201 with the created AssetDto on success,
     *         400 if the request body is invalid,
     *         401 if unauthorized,
     *         409 if a conflicting resource already exists (e.g., duplicate serial number)
     * @throws EntityNotFoundException if a required related entity is missing (e.g., referenced type not found)
     */
    @Operation(
            summary = "Create a new asset",
            description = "Creates a new asset using the provided details and returns the created resource.",
            security = { @SecurityRequirement(name = "basicAuth") },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Asset details to create",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AssetDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Asset successfully created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AssetDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request body",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    )
            }
    )
    @PostMapping("/")
    public ResponseEntity<AssetDto> createAsset(@RequestBody AssetDto assetDto) throws EntityNotFoundException, BadRequestException {
        log.info("create a new asset {}", assetDto);
        AssetIsNotValid(assetDto);
        officeService.getOfficeById(assetDto.getOffice().getId());
        assetTypeService.getAssetTypeById(assetDto.getAssetType().getId());
        return ResponseEntity.ok(service.saveAsset(assetDto));
    }


    /**
     * Moves an asset to a different office.
     *
     * @param assetId  the unique identifier of the asset to move
     * @param officeId the unique identifier of the destination office
     * @return 200 with the updated AssetDto on success,
     *         400 if one or more parameters are invalid,
     *         404 if the asset or the office does not exist,
     *         401 if unauthorized
     * @throws EntityNotFoundException if the asset or office is not found
     */
    @Operation(
            summary = "Move an asset to a different office",
            description = "Moves the asset identified by assetId to the office identified by officeId and returns the updated resource.",
            security = { @SecurityRequirement(name = "basicAuth") },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Asset successfully moved",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AssetDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameters",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Asset or office not found",
                            content = @Content
                    )
            }
    )

    @PutMapping("/move")
    public ResponseEntity<AssetDto> moveAsset(@RequestParam Long assetId, @RequestParam Long officeId) throws EntityNotFoundException {

        log.info("move asset {}", assetId);
        AssetDto assetDto;
        OfficeDto officeDto;
        officeDto = officeService.getOfficeById(officeId);
        assetDto = service.getAssetById(assetId);
        assetDto.setOffice(officeDto);

        return ResponseEntity.ok(service.saveAsset(assetDto));
    }


    /**
     * Installs (assigns) a software license to an asset.
     *
     * @param assetId   the unique identifier of the asset where the software will be installed
     * @param licenseId the unique identifier of the software license to install
     * @return 200 with the updated AssetDto on success,
     *         400 if one or more parameters are invalid,
     *         404 if the asset or the license does not exist,
     *         401 if unauthorized
     * @throws EntityNotFoundException if the asset or license is not found
     */
    @Operation(
            summary = "Install software on an asset",
            description = "Installs (assigns) the software license identified by licenseId to the asset identified by assetId and returns the updated resource.",
            security = { @SecurityRequirement(name = "basicAuth") },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Software successfully installed on asset",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AssetDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameters",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Asset or license not found",
                            content = @Content
                    )
            }
    )

    @PutMapping("/install-software")
    public ResponseEntity<AssetDto> installSoftwareAsset(@RequestParam Long assetId, @RequestParam Long licenseId) throws EntityNotFoundException {
        log.info("install software asset {}", assetId);
        AssetDto assetDto;
        SoftwareLicenseDto softwareLicenseDto;
        softwareLicenseDto = licenseService.getLicenseById(licenseId);
        assetDto = service.getAssetById(assetId);

        if (!assetDto.getSoftwareLicenses().contains(softwareLicenseDto)){
            assetDto.getSoftwareLicenses().add(softwareLicenseDto);
        }
        return ResponseEntity.ok(service.saveAsset(assetDto));
    }


    /**
     * Removes (uninstalls) a software license from an asset.
     *
     * @param assetId   the unique identifier of the asset from which the software will be removed
     * @param licenseId the unique identifier of the software license to remove
     * @return 200 with the updated AssetDto on success,
     *         400 if one or more parameters are invalid,
     *         404 if the asset or the license does not exist,
     *         401 if unauthorized
     * @throws EntityNotFoundException if the asset or license is not found
     */
    @Operation(
            summary = "Remove software from an asset",
            description = "Uninstalls (unassigns) the software license identified by licenseId from the asset identified by assetId and returns the updated resource.",
            security = { @SecurityRequirement(name = "basicAuth") },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Software successfully removed from asset",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AssetDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameters",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Asset or license not found",
                            content = @Content
                    )
            }
    )
    @PutMapping("/remove-software")
    public ResponseEntity<AssetDto> removeSoftwareAsset(@RequestParam Long assetId, @RequestParam Long licenseId) throws EntityNotFoundException {
        log.info("remove software asset {}", assetId);
        AssetDto assetDto;
        SoftwareLicenseDto softwareLicenseDto;
        softwareLicenseDto = licenseService.getLicenseById(licenseId);
        assetDto = service.getAssetById(assetId);
        assetDto.getSoftwareLicenses().remove(softwareLicenseDto);
        return ResponseEntity.ok(service.saveAsset(assetDto));
    }


    /**
     * Deletes an existing asset by ID.
     *
     * @param id the unique identifier of the asset to delete
     * @return 200 with the deleted AssetDto on success,
     *         400 if the ID is invalid,
     *         404 if the asset does not exist,
     *         401 if unauthorized
     * @throws EntityNotFoundException if the asset is not found
     */
    @Operation(
            summary = "Delete an asset by ID",
            description = "Deletes the asset identified by the given ID and returns the deleted resource.",
            security = { @SecurityRequirement(name = "basicAuth") },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Asset successfully deleted",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AssetDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid ID",
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
    @DeleteMapping("/{id}")
    public ResponseEntity<AssetDto> deleteAsset(@PathVariable Long id) throws EmptyResultDataAccessException, NoSuchElementException {
        log.info("delete asset {}", id);
        service.deleteAsset(id);
        return ResponseEntity.ok().build();

    }

    private void AssetIsNotValid(AssetDto asset) throws BadRequestException {
        if(asset.getSerialNumber() == null || asset.getSerialNumber().equals("") ||
                asset.getAssetType() == null || asset.getAssetType().getId() == null ||
                asset.getOffice() == null || asset.getOffice().getId() == null) {
            throw new BadRequestException("Invalid request parameters: Serial number, asset type, and office must exist");
        }
    }
}
