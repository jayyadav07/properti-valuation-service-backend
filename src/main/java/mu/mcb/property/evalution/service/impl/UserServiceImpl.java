package mu.mcb.property.evalution.service.impl;


import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import mu.mcb.property.evalution.constants.UserRole;
import mu.mcb.property.evalution.dto.JwtResponse;
import mu.mcb.property.evalution.dto.LoginRequest;
import mu.mcb.property.evalution.dto.TokenRefreshRequest;
import mu.mcb.property.evalution.dto.UserRequestDTO;
import mu.mcb.property.evalution.entity.RefreshToken;
import mu.mcb.property.evalution.entity.Role;
import mu.mcb.property.evalution.entity.User;
import mu.mcb.property.evalution.exception.GenericException;
import mu.mcb.property.evalution.exception.TokenRefreshException;
import mu.mcb.property.evalution.repository.RoleRepository;
import mu.mcb.property.evalution.repository.UserRepository;
import mu.mcb.property.evalution.security.JwtUtils;
import mu.mcb.property.evalution.security.RefreshTokenService;
import mu.mcb.property.evalution.security.UserDetailsImpl;
import mu.mcb.property.evalution.service.UserService;

/**
 * The Class UserServiceImpl.
 */
@Service
public class UserServiceImpl implements UserService {
	
	/** The role repository. */
	@Autowired
	private RoleRepository roleRepository;
	
	/** The user repository. */
	@Autowired
	private UserRepository userRepository;
	
	/** The authentication manager. */
	@Autowired
	private AuthenticationManager authenticationManager;
	
	/** The jwt utils. */
	@Autowired
	private JwtUtils jwtUtils;
	
	/** The refresh token service. */
	@Autowired
	private RefreshTokenService refreshTokenService;
	
	/** The encoder. */
	@Autowired
	PasswordEncoder encoder;

	/**
	 * Register user.
	 *
	 * @param userRequestDTO the user request DTO
	 * @return the string
	 */
	@Override
	@Transactional
	public Long registerUser(@Valid UserRequestDTO userRequestDTO) {
		if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
			throw new GenericException("Username is already taken!", HttpStatus.CONFLICT);
		}
		// Create new user's account
		User user =  User.builder().username(userRequestDTO.getUsername()).password(encoder.encode(userRequestDTO.getPassword()))
					.contactNumber(userRequestDTO.getContactNumber())
					.buisnessUnit(userRequestDTO.getBuisnessUnit())
					.initiatorName(userRequestDTO.getUsername()).build();
		Set<Role> roles = new HashSet<>();

		if (Objects.isNull(userRequestDTO.getRole())) {
			Role userRole = roleRepository.findByName(UserRole.USER)
					.orElseThrow(() -> new GenericException("Role not exist!", HttpStatus.NOT_FOUND));
			roles.add(userRole);
		} else {
			if (userRequestDTO.getRole().equals(UserRole.ADMIN.name())) {
				Role adminRole = roleRepository.findByName(UserRole.ADMIN)
						.orElseThrow(() -> new GenericException("Role not exist", HttpStatus.NOT_FOUND));
				roles.add(adminRole);
			} else if (userRequestDTO.getRole().equals(UserRole.USER.name())) {
				Role adminRole = roleRepository.findByName(UserRole.USER)
						.orElseThrow(() -> new GenericException("Role not exist", HttpStatus.NOT_FOUND));
				roles.add(adminRole);
			} else {
				throw new GenericException("Role not exist!", HttpStatus.NOT_FOUND);
			}
		}
		user.setRoles(roles);
		userRepository.save(user);
		return user.getId();
	}

	/**
	 * Authenticate user.
	 *
	 * @param loginRequest the login request
	 * @return the jwt response
	 */
	@Override
	public JwtResponse authenticateUser(@Valid LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
		Optional<User> optionalUser = userRepository.findById(userDetails.getId());
		return new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(), userDetails.getUsername(),
				userDetails.getRoleId(), optionalUser.get().getBuisnessUnit());
	}

	/**
	 * Signout.
	 */
	@Override
	public void signout() {
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Long userId = userDetails.getId();
		refreshTokenService.deleteByUserId(userId);

	}

	@Override
	public JwtResponse refreshToken(@Valid TokenRefreshRequest request) {
		return refreshTokenService.findByToken(request.getRefreshToken())
		        .map(refreshTokenService::verifyExpiration)
		        .map(RefreshToken::getUser)
		        .map(user -> {
		          String token = jwtUtils.generateTokenFromUsername(user.getUsername());
		          Long roleId = user.getRoles().stream().findFirst().get().getRoleId();
		          return new JwtResponse(token, request.getRefreshToken(), user.getId(), user.getUsername(), roleId);
		        })
		        .orElseThrow(() -> new TokenRefreshException(request.getRefreshToken(),
		            "Refresh token is not in database!"));
	}

}
