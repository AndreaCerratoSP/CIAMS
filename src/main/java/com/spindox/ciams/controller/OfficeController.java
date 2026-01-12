package com.spindox.ciams.controller;

import com.spindox.ciams.config.GlobalExceptionHandler;
import com.spindox.ciams.dto.OfficeDto;
import com.spindox.ciams.service.OfficeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping ("/offices")
public class OfficeController {

    @Autowired
    private OfficeService service;

    @Autowired
    private GlobalExceptionHandler exceptionHandler;


    /**
     * Retrieves the details of an office by its identifier.
     *
     * @param id the technical identifier of the office
     * @return 200 with OfficeDto on success, 404 if not found
     */
    @Operation(
            summary = "Get office by ID",
            description = "Returns the details of the office associated with the specified ID.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Office identifier",
                            required = true,
                            schema = @Schema(type = "integer", format = "int64")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Office found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OfficeDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid ID",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Office not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<OfficeDto> getOfficeById(@PathVariable Long id) {
        log.info("Get office by ID {}", id);
        return ResponseEntity.ok(service.getOfficeById(id));
    }

    /**
     * Retrieves an office by its name.
     *
     * @param name the name of the office to search for
     * @return 200 with OfficeDto if found, 404 if no office matches the given name
     */
    @Operation(
            summary = "Get office by name",
            description = "Returns the details of the office associated with the specified name.",
            parameters = {
                    @Parameter(
                            name = "name",
                            description = "Office name",
                            required = true,
                            example = "Headquarters",
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Office found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OfficeDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid name",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Office not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/name/{name}")
    public ResponseEntity<OfficeDto> getOfficesByName(@PathVariable String name) {
        log.info("Get office by name {}", name);
        return ResponseEntity.ok(service.getOfficeByName(name));
    }

    /**
     * Retrieves all offices.
     *
     * @return 200 with a list of OfficeDto objects
     */
    @Operation(
            summary = "Get all offices",
            description = "Returns a list of all offices available in the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of offices retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OfficeDto.class)
                            )
                    )
            }
    )
    @GetMapping("/")
    public ResponseEntity<List<OfficeDto>> getAllOffices() {
        log.info("Get all offices");
        return ResponseEntity.ok(service.getAllOffices());
    }

    /**
     * Creates a new office.
     *
     * @param office the OfficeDto containing the office details to create
     * @return 201 with the created OfficeDto on success, 400 if the request body is invalid
     */
    @Operation(
            summary = "Create a new office",
            description = "Creates a new office using the provided details and returns the created resource.",
            security = { @SecurityRequirement(name = "basicAuth") },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Office details to create",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OfficeDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Office successfully created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OfficeDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request body",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Authentication Failed",
                            content = @Content
                    )
            }
    )
    @PostMapping("/")
    public ResponseEntity<OfficeDto> createOffice(@RequestBody OfficeDto office) throws BadRequestException {
        log.info("Create office {}", office);
        OfficeIsNotValid(office);
        return ResponseEntity.ok(service.saveOffice(office));
    }

    /**
     * Updates an existing office by its ID.
     *
     * @param office the OfficeDto containing the updated office details
     * @param id     the identifier of the office to update
     * @return 200 with the updated OfficeDto on success, 400 if the request body or ID is invalid,
     *         404 if the office does not exist
     */
    @Operation(
            summary = "Update an office by ID",
            description = "Updates the office identified by the given ID with the provided details and returns the updated resource.",
            security = { @SecurityRequirement(name = "basicAuth") },
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Office identifier",
                            required = true,
                            schema = @Schema(type = "integer", format = "int64")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated office details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OfficeDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Office successfully updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OfficeDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid ID or request body",
                            content = @Content // optionally define an ErrorDto schema
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Office not found",
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
    public ResponseEntity<OfficeDto> updateOffice(@RequestBody OfficeDto office, @PathVariable Long id) throws BadRequestException {
        log.info("Update office {}", office);
        OfficeIsNotValid(office);
        OfficeDto newOffice = service.getOfficeById(id);
        newOffice.setName(office.getName());
        return ResponseEntity.ok(service.saveOffice(newOffice));
    }


    /**
     * Deletes an office by its ID.
     *
     * @param id the identifier of the office to delete
     * @return 204 on successful deletion, 404 if the office does not exist
     */
    @Operation(
            summary = "Delete an office by ID",
            description = "Deletes the office identified by the given ID.",
            security = { @SecurityRequirement(name = "basicAuth") },
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Office identifier",
                            required = true,
                            schema = @Schema(type = "integer", format = "int64")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Office successfully deleted",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Office not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    )

            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffice(@PathVariable Long id) throws EmptyResultDataAccessException, NoSuchElementException{
        log.info("Delete office {}", id);
        service.deleteOffice(id);
        return ResponseEntity.ok().build();
    }

    private void OfficeIsNotValid(OfficeDto office) throws BadRequestException {
        List<OfficeDto> existingOfficies = service.getAllOffices();
        for (OfficeDto existingOffice : existingOfficies) {
            if  (existingOffice.getName().equals(office.getName())) {
                throw  new BadRequestException("Another office with the same name already exists");
            }
        }
        if(office.getName() == null || office.getName().equals("")) {
            throw new BadRequestException("Invalid id: must be a positive number");
        }
    }
}


