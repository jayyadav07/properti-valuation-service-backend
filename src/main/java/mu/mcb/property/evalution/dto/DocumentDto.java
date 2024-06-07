package mu.mcb.property.evalution.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentDto {
    private Long id;
    private DocumentTypeDto type;
    private byte[] document;    
    private String fileName;    
    private Long fileSize;
    private LocalDateTime createdDate;
    private String updatedBy;
    private String documentType;
	
}
