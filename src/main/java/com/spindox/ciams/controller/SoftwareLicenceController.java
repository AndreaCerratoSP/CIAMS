package com.spindox.ciams.controller;

import com.spindox.ciams.dto.SoftwareLicenseDto;
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
@RequestMapping("/softwarelicences")
public class SoftwareLicenceController {

    @Autowired
    SoftwareLicenceService service;

    @GetMapping("/{id}")
    public ResponseEntity<SoftwareLicenseDto> getSoftwareLicenseById(@PathVariable Long id) {


        System.out.println("Dentro controller id = " + id);
        try {
            return ResponseEntity.ok(service.getLicenseById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<SoftwareLicenseDto> getSoftwareLicenseByName(@PathVariable String name) {
        try {
            return ResponseEntity.ok(service.getLicenseByName(name));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<SoftwareLicenseDto>> getAllSoftwareLicense() {
        return ResponseEntity.ok(service.getAllLicenses());
    }


    @PostMapping("/")
    public ResponseEntity<SoftwareLicenseDto> createSoftwareLicense(@RequestBody SoftwareLicenseDto licence) {
        if(LicenceIsNotValid(licence)) {
            return  ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.saveLicense(licence));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SoftwareLicenseDto> updateSoftwareLicense(@RequestBody SoftwareLicenseDto licence, @PathVariable Long id) {
        if(LicenceIsNotValid(licence)) {
            return  ResponseEntity.badRequest().build();
        }

        try{
            SoftwareLicenseDto newLicence = service.getLicenseById(id);
            newLicence.setName(licence.getName());

            return ResponseEntity.ok(service.saveLicense(newLicence));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSoftwareLicence(@PathVariable Long id) {
        try {
            service.deleteLicense(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException | NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }

    }


    private boolean LicenceIsNotValid(SoftwareLicenseDto licence) {
        if(licence.getName() == null || licence.getName().equals("")) {
            return true;
        }
        return false;
    }

}
