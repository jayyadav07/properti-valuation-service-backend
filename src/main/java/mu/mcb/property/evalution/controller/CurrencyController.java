package mu.mcb.property.evalution.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import mu.mcb.property.evalution.dto.CurrencyDto;
import mu.mcb.property.evalution.service.CurrencyService;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/currency")
public class CurrencyController {

	@Autowired
    private CurrencyService currencyService;
	
    @GetMapping("/currencies")
    public ResponseEntity<List<CurrencyDto>> getCurrencies() {
    	log.info("CurrencyController getCurrencies method is called.");
        return new ResponseEntity<>(currencyService.getCurrencies(), HttpStatus.OK);
    }
}
