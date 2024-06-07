package mu.mcb.property.evalution.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import mu.mcb.property.evalution.dto.CustomResponse;
import mu.mcb.property.evalution.dto.JwtResponse;
import mu.mcb.property.evalution.dto.LoginRequest;
import mu.mcb.property.evalution.dto.TokenRefreshRequest;
import mu.mcb.property.evalution.dto.UserRequestDTO;
import mu.mcb.property.evalution.service.UserService;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	/** The user service. */
	@Autowired
	private UserService userService;

	@PostMapping("/registerUser")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
		log.info("AuthController registerUser method is called.");
		Long userId = userService.registerUser(userRequestDTO);
		return new ResponseEntity<>(new CustomResponse("User registered successfully", userId), HttpStatus.CREATED);
	}

	/**
	 * Authenticate user.
	 * @param loginRequest the login request
	 * @return the response entity
	 */
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		log.info("AuthController authenticateUser method is called.");
		log.debug("UserID : {}, Password : ",loginRequest.getUsername(),loginRequest.getPassword());
		JwtResponse jwtResponse = userService.authenticateUser(loginRequest);
		return new ResponseEntity<>(jwtResponse, HttpStatus.OK);

	}
	
	/**
	 * Logout user.
	 * @return the response entity
	 */
	@GetMapping("/logout")
	public ResponseEntity<?> logoutUser() {
		userService.signout();
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * Refreshtoken.
	 * @param request the request
	 * @return the response entity
	 */
	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
		return new ResponseEntity<>(userService.refreshToken(request), HttpStatus.OK);
	}
}
