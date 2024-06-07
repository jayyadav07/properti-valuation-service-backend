package mu.mcb.property.evalution.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class PropertyValuationDto {

    private Long id;
    private UserDto createdBy;
    private String fosreferenceNumber;
    private String type;
    private String mainBorrowerName;
    private String referanceNo;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private FacilityDto facilityDto;
    private List<BorrowerDto> borrowersDto;
    private List<DocumentDto> documentsDto;
    private List<CommentsDto> commentsDto;
	
}
