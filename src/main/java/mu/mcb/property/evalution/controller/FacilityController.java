package mu.mcb.property.evalution.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mu.mcb.property.evalution.dto.FacilityTypeDto;
import mu.mcb.property.evalution.service.FacilityService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/facility")
public class FacilityController {

	
	@Autowired
	private FacilityService facilityService;
	
	@SuppressWarnings("unused")
	@GetMapping("/facilitytypes")
	public ResponseEntity<List<FacilityTypeDto>> getFacilityTypes() {
		return new ResponseEntity<>(facilityService.getFacilityTypes(), HttpStatus.OK);
	}
}
