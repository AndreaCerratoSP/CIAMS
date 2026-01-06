package com.spindox.ciams.controller;

import com.spindox.ciams.dto.SoftwareLicenseDto;
import com.spindox.ciams.service.SoftwareLicenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/softwarelicences")
public class SoftwareLicenseController {

    @Autowired
    SoftwareLicenceService service;


    /**
     * Retrieves the details of a software license by its identifier.
     *
     * @param id the technical identifier of the software license
     * @return 200 with SoftwareLicenseDto on success, 404 if the license is not found
     */
    @Operation(
            summary = "Get software license by ID",
            description = "Returns the details of the software license associated with the specified ID.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Software license identifier",
                            required = true,
                            schema = @Schema(type = "integer", format = "int64")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Software license found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SoftwareLicenseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid ID",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Software license not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<SoftwareLicenseDto> getSoftwareLicenseById(@PathVariable Long id) {

        log.info("getSoftwareLicenseById {}", id);
        try {
            return ResponseEntity.ok(service.getLicenseById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Retrieves software licenses by name.
     *
     * @param name the name (or partial name) of the software license to search for
     * @return 200 with a list of SoftwareLicenseDto objects if matches are found,
     *         404 if no licenses match the given name,
     *         400 if the provided name is invalid
     */
    @Operation(
            summary = "Get software licenses by name",
            description = "Returns a list of software licenses whose name matches the specified value.",
            parameters = {
                    @Parameter(
                            name = "name",
                            description = "Software license name to search for",
                            required = true,
                            example = "Visual Studio",
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Licenses retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SoftwareLicenseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid name",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No licenses found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/name/{name}")
    public ResponseEntity<List<SoftwareLicenseDto>> getSoftwareLicenseByName(@PathVariable String name) {
        log.info("getSoftwareLicenseByName {}", name);
        return ResponseEntity.ok(service.getLicenseByName(name));
    }

    /**
     * Retrieves all software licenses.
     *
     * @return 200 with a list of SoftwareLicenseDto objects
     */
    @Operation(
            summary = "Get all software licenses",
            description = "Returns a list of all software licenses available in the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of software licenses retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SoftwareLicenseDto.class))
                            )
                    )
            }
    )
    @GetMapping("/")
    public ResponseEntity<List<SoftwareLicenseDto>> getAllSoftwareLicense() {
        log.info("getAllSoftwareLicense");
        return ResponseEntity.ok(service.getAllLicenses());
    }

    /**
     * Retrieves all software licenses that are expiring soon.
     *
     * @return 200 with a list of SoftwareLicenseDto objects representing licenses close to expiration
     */
    @Operation(
            summary = "Get expiring software licenses",
            description = "Returns a list of software licenses that are approaching their expiration date.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of expiring software licenses retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SoftwareLicenseDto.class))
                            )
                    )
            }
    )
    @GetMapping("/expiring")
    public ResponseEntity<List<SoftwareLicenseDto>> getExpireDate() {
        log.info("getExpireDate");
        return ResponseEntity.ok(service.getLicenseWithExpiringDates());
    }


    /**
     * Creates a new software license.
     *
     * @param licence the SoftwareLicenseDto containing the license details to create
     * @return 201 with the created SoftwareLicenseDto on success, 400 if the request body is invalid
     */
    @Operation(
            summary = "Create a new software license",
            description = "Creates a new software license using the provided details and returns the created resource.",
            security = { @SecurityRequirement(name = "basicAuth") },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Software license details to create",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SoftwareLicenseDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Software license successfully created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SoftwareLicenseDto.class)
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
    public ResponseEntity<SoftwareLicenseDto> createSoftwareLicense(@RequestBody SoftwareLicenseDto licence) {
        log.info("createSoftwareLicense {}", licence);
        if(LicenceIsNotValid(licence)) {
            return  ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.saveLicense(licence));
    }


    /**
     * Updates an existing software license by its ID.
     *
     * @param licence the SoftwareLicenseDto containing the updated license details
     * @param id      the identifier of the software license to update
     * @return 200 with the updated SoftwareLicenseDto on success,
     *         400 if the request body or ID is invalid,
     *         404 if the software license does not exist
     */
    @Operation(
            summary = "Update a software license by ID",
            description = "Updates the software license identified by the given ID with the provided details and returns the updated resource.",
            security = { @SecurityRequirement(name = "basicAuth") },
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Software license identifier",
                            required = true,
                            schema = @Schema(type = "integer", format = "int64")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated software license details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SoftwareLicenseDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Software license successfully updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SoftwareLicenseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid ID or request body",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Software license not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<SoftwareLicenseDto> updateSoftwareLicense(@RequestBody SoftwareLicenseDto licence, @PathVariable Long id) {
        log.info("updateSoftwareLicense {}", licence);
        if(LicenceIsNotValid(licence)) {
            return  ResponseEntity.badRequest().build();
        }

        try{
            SoftwareLicenseDto newLicence = service.getLicenseById(id);
            newLicence.setName(licence.getName());
            newLicence.setExpireDate(licence.getExpireDate());
            return ResponseEntity.ok(service.saveLicense(newLicence));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }

    /**
     * Deletes a software license by its ID.
     *
     * @param id the identifier of the software license to delete
     * @return 204 on successful deletion, 404 if the software license does not exist
     */
    @Operation(
            summary = "Delete a software license by ID",
            description = "Deletes the software license identified by the given ID.",
            security = { @SecurityRequirement(name = "basicAuth") },
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Software license identifier",
                            required = true,
                            schema = @Schema(type = "integer", format = "int64")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Software license successfully deleted",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Software license not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSoftwareLicence(@PathVariable Long id) {
        log.info("deleteSoftwareLicence {}", id);
        try {
            service.deleteLicense(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException | NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }

    }


    private boolean LicenceIsNotValid(SoftwareLicenseDto licence) {
        if(licence.getName() == null || licence.getName().equals("") || licence.getExpireDate() == null) {
            return true;
        }
        return false;
    }

}
