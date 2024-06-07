package mu.mcb.property.evalution.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import mu.mcb.property.evalution.dto.PropertyValuationDto;
import mu.mcb.property.evalution.entity.PropertyValuation;
import mu.mcb.property.evalution.service.PropertyValuationService;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/propertyval")
public class PropertyValuationController {

    @Autowired
    private PropertyValuationService propertyValuationService;

    @PostMapping("/evaluation-form")
    public ResponseEntity<PropertyValuation> createValuationApplication(@RequestParam("file") MultipartFile file, @RequestParam("appData") String appData) throws JsonProcessingException {
    	log.info("PropertyValuationController createValuationApplication method is called.");
    	log.debug("Payload String format : {}",appData);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        PropertyValuationDto pvAppDto = mapper.readValue(appData, PropertyValuationDto.class);
        return new ResponseEntity<>(propertyValuationService.createEvaluationApplication(pvAppDto,file), HttpStatus.OK);
    }
    
    @PutMapping(path = "/evaluation-form-update")
    public ResponseEntity<String> updateEvaluationApplication(@Valid @RequestBody PropertyValuationDto pvAppDto){
    	log.info("PropertyValuationController updateEvaluationApplication method is called.");
    	propertyValuationService.updateEvaluationApplication(pvAppDto);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
    
    @GetMapping("/fetchapplications")
    public ResponseEntity<List<PropertyValuationDto>> fetchAllApplication(){
    	log.info("PropertyValuationController fetchAllApplication method is called.");
        return new ResponseEntity<>(propertyValuationService.fetchApplication(), HttpStatus.OK);
    }

    @GetMapping("/fetchapplication/{id}")
    public ResponseEntity<PropertyValuationDto> fetchAByApplicationId(@PathVariable Long id){
    	log.info("PropertyValuationController updateEvaluationApplication method is called.");
    	log.debug("Application ID : {}",id);
        return new ResponseEntity<>(propertyValuationService.fetchApplicationById(id), HttpStatus.OK);
    }

}
