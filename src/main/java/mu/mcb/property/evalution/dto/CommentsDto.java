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
public class CommentsDto {
    private Long id;
    private String comments;
    private LocalDateTime createdDate;
    private String userName;
    private String createdDates;
	
}
