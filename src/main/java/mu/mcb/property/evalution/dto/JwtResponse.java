package mu.mcb.property.evalution.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
	private String accessToken = "Bearer";
	private String refreshToken;
	private Long id;
	private String username;
	private Long roleId;
	private String businessUnit;
	
	public JwtResponse(String accessToken, String refreshToken, Long id, String username, Long roleId) {
		super();
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.id = id;
		this.username = username;
		this.roleId = roleId;
	}
	
}
