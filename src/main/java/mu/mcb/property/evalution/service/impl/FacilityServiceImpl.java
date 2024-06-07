package mu.mcb.property.evalution.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mu.mcb.property.evalution.dto.FacilityTypeDto;
import mu.mcb.property.evalution.entity.FacilityType;
import mu.mcb.property.evalution.repository.FacilityTypeRepository;
import mu.mcb.property.evalution.service.FacilityService;

@Slf4j
@Service
public class FacilityServiceImpl implements FacilityService{

	@Autowired
	private FacilityTypeRepository facilityTypeRepository;

	@Override
    public List<FacilityTypeDto> getFacilityTypes() {
		log.info("FacilityServiceImpl getFacilityTypes method is called.");
        List<FacilityTypeDto> facilityTypeResponseList = new ArrayList<>();
        try {
            List<FacilityType> facilityTypeList = facilityTypeRepository.findAll();
            facilityTypeList.forEach(facilityType -> {
                FacilityTypeDto facilityTypeResponse = new FacilityTypeDto();
                facilityTypeResponse.setId(facilityType.getId());
                facilityTypeResponse.setName(facilityType.getName());
                facilityTypeResponseList.add(facilityTypeResponse);
            });
        } catch (RuntimeException ex) {
        	log.info("RuntimeException cought : {}",ex.getMessage());
        } catch (Exception ex) {
        	log.info("Genric Exception cought : {}",ex.getMessage());
        }
        return facilityTypeResponseList;
    }
	
}
