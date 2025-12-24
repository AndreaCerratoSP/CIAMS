package com.spindox.ciams.controller;

import com.spindox.ciams.dto.OfficeDto;
import com.spindox.ciams.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/offices")
public class OfficeController {

    @Autowired
    private OfficeService service;

    @GetMapping("/{id}")
    public OfficeDto getOfficeById(@PathVariable Long id) {

        System.out.println("Dentro controller id = " + id);
        return service.getOfficeById(id);
    }


}
