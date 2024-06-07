package mu.mcb.property.evalution.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import mu.mcb.property.evalution.dto.PropertyValuationDto;
import mu.mcb.property.evalution.entity.PropertyValuation;

public interface PropertyValuationService {
    
	PropertyValuation createEvaluationApplication(PropertyValuationDto pvAppDto, MultipartFile file);
   
	String updateEvaluationApplication(PropertyValuationDto pvAppDto);
    
	List<PropertyValuationDto> fetchApplication();

    PropertyValuationDto fetchApplicationById(Long id);
}
