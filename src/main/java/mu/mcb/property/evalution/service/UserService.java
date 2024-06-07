package mu.mcb.property.evalution.service;


import jakarta.validation.Valid;
import mu.mcb.property.evalution.dto.JwtResponse;
import mu.mcb.property.evalution.dto.LoginRequest;
import mu.mcb.property.evalution.dto.TokenRefreshRequest;
import mu.mcb.property.evalution.dto.UserRequestDTO;

/**
 * The Interface UserService.
 */
public interface UserService {

	Long registerUser(@Valid UserRequestDTO userRequestDTO);

	JwtResponse authenticateUser(@Valid LoginRequest loginRequest);

	void signout();

	JwtResponse refreshToken(@Valid TokenRefreshRequest request);

}
