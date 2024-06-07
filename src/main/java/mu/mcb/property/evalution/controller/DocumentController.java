package mu.mcb.property.evalution.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mu.mcb.property.evalution.dto.DocumentDto;
import mu.mcb.property.evalution.dto.DocumentTypeDto;
import mu.mcb.property.evalution.service.DocumentService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {

	@Autowired
	private DocumentService documentService;
	
    @GetMapping("/documenttypes")
    public ResponseEntity<List<DocumentTypeDto>> getDocumentTypes() {
        return new ResponseEntity<>(documentService.getDocumentTypes(), HttpStatus.OK);
    }
    
    @GetMapping("/documents/{userId}")
    public ResponseEntity<List<DocumentDto>> getDocumentTypes(@PathVariable Long userId) {
        return new ResponseEntity<>(documentService.getDocuments(userId), HttpStatus.OK);
    }
}
