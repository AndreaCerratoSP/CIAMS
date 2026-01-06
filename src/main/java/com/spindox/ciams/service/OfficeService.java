package com.spindox.ciams.service;

import com.spindox.ciams.dto.OfficeDto;
import com.spindox.ciams.mapper.OfficeMapper;
import com.spindox.ciams.model.Office;
import com.spindox.ciams.repository.OfficeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OfficeService {

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private OfficeMapper officeMapper;

    /**
     * Find all Officies
     *
     * @return List of officies
     */
    public List<OfficeDto> getAllOffices(){
        log.info("Inside the service, getAllOffices method");
        List<Office> offices = officeRepository.findAll();
        return officeMapper.toDto(offices);
    }

    /**
     * Finds an office by his id
     *
     * @param id of the office to search
     * @return the foud office
     * @throws EntityNotFoundException when office is not found
     */
    public OfficeDto getOfficeById(Long id) throws EntityNotFoundException{

        log.info("Inside the service, getOfficeById method");
        Optional<Office> officeOpt =  officeRepository.findById(id);
        if(officeOpt.isPresent()){
            Office office = officeOpt.get();
            log.info("office found with id {}", office.getId());
            return officeMapper.toDto(office);
        }
        else{
            log.error("Office not found with id {}", id);
            throw  new EntityNotFoundException("Office with id " + id + " not found");
        }

    }

    /**
     * Saves an office on the batabase
     *
     * @param officeDto is the dto that get mapped in the entity saved
     * @return that dto of the office
     */
    public OfficeDto saveOffice(OfficeDto officeDto){
        Office office = officeMapper.fromDto(officeDto);
        officeRepository.save(office);
        log.info("office saved with id {}", office.getId());
        return officeMapper.toDto(office);
    }

    /**
     * Deletes an office from the database
     *
     * @param id is the pk of the office
     */
    public void deleteOffice(Long id){
        Office office = officeRepository.findById(id).get();
        log.info("office deleted with id {}", office.getId());
        officeRepository.delete(office);
    }

    /**
     * Finds an office by his name
     *
     * @param name is the name of the office
     * @return the dto of the office
     * @throws EntityNotFoundException when the office doesn't get found
     */
    public OfficeDto getOfficeByName(String name) throws EntityNotFoundException{

        log.info("Inside the service, getOfficeByName method");
        Optional<Office> officeOpt =  officeRepository.findOfficeByName(name);
        if(officeOpt.isPresent()){
            Office office = officeOpt.get();
            log.info("office found with name {}", office.getName());
            return officeMapper.toDto(office);
        }
        else{
            log.error("Office not found with name {}", name);
            throw  new EntityNotFoundException("Office with name " + name + " not found");
        }

    }

}
