package com.spindox.ciams.service;

import com.spindox.ciams.dto.OfficeDto;
import com.spindox.ciams.mapper.OfficeMapper;
import com.spindox.ciams.model.Office;
import com.spindox.ciams.repository.OfficeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfficeService {

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private OfficeMapper officeMapper;

    public List<OfficeDto> getAllOffices(){
        List<Office> offices = officeRepository.findAll();
        return officeMapper.toDto(offices);
    }

    public OfficeDto getOfficeById(Long id){

        System.out.println("Dentro service");
        Optional<Office> officeOpt =  officeRepository.findById(id);
        if(officeOpt.isPresent()){
            Office office = officeOpt.get();
            System.out.println("Dentro service, trovato");
            return officeMapper.toDto(office);
        }
        else{
            System.out.println("Dentro service, non trovato");
            throw  new EntityNotFoundException("Office with id " + id + " not found");
        }

    }

    public OfficeDto saveOffice(OfficeDto officeDto){
        Office office = officeMapper.fromDto(officeDto);
        officeRepository.save(office);
        return officeMapper.toDto(office);
    }
}
