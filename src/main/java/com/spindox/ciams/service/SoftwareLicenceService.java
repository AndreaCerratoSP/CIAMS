package com.spindox.ciams.service;

import com.spindox.ciams.dto.SoftwareLicenseDto;
import com.spindox.ciams.mapper.SoftwareLicenseMapper;
import com.spindox.ciams.model.SoftwareLicense;
import com.spindox.ciams.repository.SoftwareLicenseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SoftwareLicenceService {


    @Autowired
    private SoftwareLicenseRepository softwareLicenseRepository;

    @Autowired
    private SoftwareLicenseMapper softwareLicenseMapper;

    /**
     * Find all Software Licenses
     *
     * @return List of software licenses
     */
    public List<SoftwareLicenseDto> getAllLicenses() {
        log.info("Inside the service, getAllLicenses method");
        List<SoftwareLicense> licenses = softwareLicenseRepository.findAll();
        return softwareLicenseMapper.toDto(licenses);
    }

    /**
     * Finds a software license by its id
     *
     * @param id of the license to search
     * @return the found license
     * @throws EntityNotFoundException when license is not found
     */
    public SoftwareLicenseDto getLicenseById(Long id) throws EntityNotFoundException {
        log.info("Inside the service, getLicenseById method");
        Optional<SoftwareLicense> licenseOpt = softwareLicenseRepository.findById(id);
        if (licenseOpt.isPresent()) {
            log.info("licenseOpt found with id {}", id);
            return softwareLicenseMapper.toDto(licenseOpt.get());
        } else {
            log.info("licenseOpt not found with id {}", id);
            throw new EntityNotFoundException("SoftwareLicense with id " + id + " not found");
        }
    }

    /**
     * Saves a software license on the database
     *
     * @param softwareLicenseDto is the dto that gets mapped into the entity saved
     * @return the dto of the saved license
     */
    public SoftwareLicenseDto saveLicense(SoftwareLicenseDto softwareLicenseDto) {
        SoftwareLicense softwareLicense = softwareLicenseMapper.fromDto(softwareLicenseDto);
        softwareLicenseRepository.save(softwareLicense);
        log.info("softwareLicense saved with id {}", softwareLicense.getId());
        return softwareLicenseMapper.toDto(softwareLicense);
    }

    /**
     * Deletes a software license from the database
     *
     * @param id is the pk of the license
     */
    public void deleteLicense(Long id) {

        Optional<SoftwareLicense> licenseOpt =  softwareLicenseRepository.findById(id);
        if(licenseOpt.isPresent()){
            SoftwareLicense license = licenseOpt.get();
            log.info("softwareLicense deleted with id {}", id);
            softwareLicenseRepository.delete(license);
        }
        else{
            log.error("Office not found with id {}", id);
            throw  new EntityNotFoundException("softwareLicense with id " + id + " not found");
        }
    }

    /**
     * Finds a software license by its name
     *
     * @param name is the name of the license
     * @return the dto of the license
     * @throws EntityNotFoundException when the license doesn't get found
     */
    public List<SoftwareLicenseDto> getLicenseByName(String name) throws EntityNotFoundException {
        log.info("Inside the service, getLicenseByName method");
        List<SoftwareLicense> Listlicense = softwareLicenseRepository.findSoftwareLicenseByNameContainingOrderByName(name);
        return softwareLicenseMapper.toDto(Listlicense);
    }

    /**
     * Finds all the software licenses that are expiring in the next 30 days
     *
     * @return the list of the software licenses expiring
     */
    public List<SoftwareLicenseDto> getLicenseWithExpiringDates() {

        log.info("Inside the service, getLicenseWithExpiringDates method");
        Instant today = Instant.now();
        Instant in30   = today.plus(30, ChronoUnit.DAYS);
        Timestamp start = Timestamp.from(today);
        Timestamp end   = Timestamp.from(in30);

        List<SoftwareLicense> licenses = softwareLicenseRepository.findSoftwareLicenseByExpireDateBetween(start, end);
        return softwareLicenseMapper.toDto(licenses);
    }

}

