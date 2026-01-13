package com.spindox.ciams.controller;

import com.spindox.ciams.dto.AssetTypeDto;
import com.spindox.ciams.service.AssetTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/AssetTypes")
public class AssetTypeController {

    @Autowired
    private AssetTypeService service;

    /**
     * Retrieves all asset types.
     *
     * @return 200 with a list of AssetTypeDto objects
     */
    @Operation(
            summary = "Get all asset types",
            description = "Returns a list of all asset types available in the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of asset types retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssetTypeDto.class))
                            )
                    )
            }
    )
    @GetMapping("/")
    public ResponseEntity<List<AssetTypeDto>> getAllAssetTypes(){
        log.info("Get all the Asset Types");
        return ResponseEntity.ok(service.getAllAssetTypes());
    }

    /**
     * Retrieves an asset type by its ID.
     *
     * @param id the identifier of the asset type
     * @return 200 with AssetTypeDto on success, 404 if the asset type does not exist
     */
    @Operation(
            summary = "Get asset type by ID",
            description = "Returns the details of the asset type associated with the specified ID.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Asset type identifier",
                            required = true,
                            schema = @Schema(type = "integer", format = "int64")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Asset type found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AssetTypeDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid ID",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Asset type not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<AssetTypeDto> getAssetTypeById(@PathVariable long id) throws  EntityNotFoundException {
        log.info("Get asset type by ID {}", id);
        return ResponseEntity.ok(service.getAssetTypeById(id));
    }


    /**
     * Retrieves asset types by name.
     *
     * @param name the name (or partial name) of the asset type to search for
     * @return 200 with a list of AssetTypeDto objects if matches are found,
     *         404 if no asset types match the given name,
     *         400 if the provided name is invalid
     */
    @Operation(
            summary = "Get asset types by name",
            description = "Returns a list of asset types whose name matches the specified value.",
            parameters = {
                    @Parameter(
                            name = "name",
                            description = "Asset type name to search for",
                            required = true,
                            example = "Laptop",
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Asset types retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssetTypeDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid name",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No asset types found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/name/{name}")
    public ResponseEntity<List<AssetTypeDto>> getAssetTypeByName(@PathVariable String name) {
        log.info("Get asset type by name {}", name);
        return ResponseEntity.ok(service.getAssetTypeByName(name));
    }

    /**
     * Creates a new asset type.
     *
     * @param assetType the AssetTypeDto containing the asset type details to create
     * @return 200 with the created AssetTypeDto on success, 400 if the request body is invalid
     */
    @Operation(
            summary = "Create a new asset type",
            description = "Creates a new asset type using the provided details and returns the created resource.",
            security = { @SecurityRequirement(name = "basicAuth") },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Asset type details to create",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AssetTypeDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Asset type successfully created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AssetTypeDto.class)
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
    @PostMapping
    public ResponseEntity<AssetTypeDto> createAssetType(@RequestBody AssetTypeDto assetType) throws BadRequestException {
        log.info("Create new asset type {}", assetType);
        AssetTypeIsNotValid(assetType);
        return ResponseEntity.ok(service.saveAssetType(assetType));
    }

    /**
     * Updates an existing asset type.
     *
     * @param assetType the AssetTypeDto containing the updated asset type details
     * @param id the unique identifier of the asset type to update
     * @return 200 with the updated AssetTypeDto on success,
     *         400 if the request body is invalid
     *         404 if the asset type does not exist,
     *         401 if unauthorized
     */
    @Operation(
            summary = "Update an existing asset type",
            description = "Updates the asset type identified by the given ID using the provided details and returns the updated resource.",
            security = { @SecurityRequirement(name = "basicAuth") },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Asset type details to update",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AssetTypeDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Asset type successfully updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AssetTypeDto.class)
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
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Asset type not found",
                            content = @Content
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<AssetTypeDto> updateAssetType(@RequestBody AssetTypeDto assetType, @PathVariable Long id) throws BadRequestException {
        log.info("Update an existing asset type {}", id);
        AssetTypeIsNotValid(assetType);
        AssetTypeDto newAssetType = service.getAssetTypeById(id);
        newAssetType.setName(assetType.getName());
        newAssetType.setDescription(assetType.getDescription());
        return ResponseEntity.ok(service.saveAssetType(newAssetType));
    }

    /**
     * Deletes an existing asset type by ID.
     *
     * @param id the unique identifier of the asset type to delete
     * @return 200 with the deleted AssetTypeDto on success,
     *         404 if the asset type does not exist,
     *         401 if unauthorized
     */
    @Operation(
            summary = "Delete an existing asset type",
            description = "Deletes the asset type identified by the given ID and returns the deleted resource.",
            security = { @SecurityRequirement(name = "basicAuth") },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Asset type successfully deleted",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AssetTypeDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Asset type not found",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<AssetTypeDto> deleteAssetType(@PathVariable Long id) {
        log.info("Delete an existing asset type {}", id);
        service.deleteAssetType(id);
        return ResponseEntity.ok().build();
    }

    private void AssetTypeIsNotValid(AssetTypeDto assetType) throws BadRequestException {
        if(assetType.getName() == null || assetType.getName().equals("")) {
            throw new BadRequestException("Name must not be empty");
        }
    }

}
