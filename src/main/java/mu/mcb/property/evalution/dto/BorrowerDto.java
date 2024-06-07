package mu.mcb.property.evalution.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowerDto {
    private Long id;
    private String name;
    private String customerNumber;
    private String contactNumber;
    private String email;
    private String address;
    private Boolean isMainBorrower;
	
    
}
