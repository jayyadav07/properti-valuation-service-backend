package mu.mcb.property.evalution.service;

import java.util.List;

import mu.mcb.property.evalution.dto.DocumentDto;
import mu.mcb.property.evalution.dto.DocumentTypeDto;

public interface DocumentService {

    List<DocumentTypeDto> getDocumentTypes();

	List<DocumentDto> getDocuments(Long userId);
}
