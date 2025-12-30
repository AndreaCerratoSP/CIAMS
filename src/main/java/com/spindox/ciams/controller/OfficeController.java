package com.spindox.ciams.controller;

import com.spindox.ciams.dto.OfficeDto;
import com.spindox.ciams.service.OfficeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping ("/offices")
public class OfficeController {

    @Autowired
    private OfficeService service;

    @GetMapping("/{id}")
    public ResponseEntity<OfficeDto> getOfficeById(@PathVariable Long id) {

        System.out.println("Dentro controller id = " + id);
        try {
            return ResponseEntity.ok(service.getOfficeById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<OfficeDto> getOfficesByName(@PathVariable String name) {
        try {
            return ResponseEntity.ok(service.getOfficeByName(name));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<OfficeDto>> getAllOffices() {
        return ResponseEntity.ok(service.getAllOffices());
    }

    @PostMapping("/")
    public ResponseEntity<OfficeDto> createOffice(@RequestBody OfficeDto office) {
        if(OfficeIsNotValid(office)) {
            return  ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.saveOffice(office));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfficeDto> updateOffice(@RequestBody OfficeDto office, @PathVariable Long id) {
        if(OfficeIsNotValid(office)) {
            return  ResponseEntity.badRequest().build();
        }

        try{
            OfficeDto newOffice = service.getOfficeById(id);
            newOffice.setName(office.getName());

            return ResponseEntity.ok(service.saveOffice(newOffice));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffice(@PathVariable Long id) {
        try {
            service.deleteOffice(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException | NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }

    }

    private boolean OfficeIsNotValid(OfficeDto office) {
        if(office.getName() == null || office.getName().equals("")) {
            return true;
        }
        return false;
    }
}


