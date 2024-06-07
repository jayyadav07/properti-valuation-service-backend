package mu.mcb.property.evalution.security;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

class SecurityConfigTest {

	@InjectMocks
	private SecurityConfig securityConfig;

	@Mock
	private UserDetailsServiceImpl userDetailsService;

	@Mock
	private AuthEntryPointJwt unauthorizedHandler;

	@Mock
	private AuthenticationConfiguration authConfig;

	@Mock
	private AuthenticationManager authenticationManager;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		when(authConfig.getAuthenticationManager()).thenReturn(authenticationManager);
	}

	@Test
	void testFilterChainConfiguration() throws Exception {
		SecurityFilterChain filterChain = securityConfig.filterChain(null);

		// Verify configuration
		verify(unauthorizedHandler).getClass();
		verify(authenticationManager).getClass();

		// Assert
		assert (filterChain != null);
	}

	@Test
	void testAuthenticationProviderBean() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		when(securityConfig.passwordEncoder()).thenReturn(passwordEncoder);

		// Call authenticationProvider method
		securityConfig.authenticationProvider();

		// Verify
		verify(userDetailsService).getClass();
		verify(passwordEncoder).getClass();
	}

	// Add more test cases as needed for other methods in the SecurityConfig class
}