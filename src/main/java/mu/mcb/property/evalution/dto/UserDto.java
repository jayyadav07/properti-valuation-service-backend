package mu.mcb.property.evalution.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long userid;
    private String username;
    private Set<RoleDto> roles;
    private String buisnessUnit;
    private String contactNumber;
	
    
}
