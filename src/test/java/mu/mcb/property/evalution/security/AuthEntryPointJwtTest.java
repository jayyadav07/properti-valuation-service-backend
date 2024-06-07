package mu.mcb.property.evalution.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class AuthEntryPointJwtTest {

	@InjectMocks
	private AuthEntryPointJwt authEntryPointJwt;

	@Mock
	private ObjectMapper objectMapper;

	private final AuthenticationException authException = new AuthenticationException("Unauthorized") {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	};

	@Test
	void testCommence() throws IOException, ServletException {
		// Initialize mocks
		MockitoAnnotations.openMocks(this);

		// Prepare request and response objects
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();

		// Define the expected response body
		Map<String, Object> expectedBody = Map.of("status", HttpServletResponse.SC_UNAUTHORIZED, "error",
				"Unauthorized", "message", authException.getMessage(), "path", request.getServletPath());

		// Mock ObjectMapper behavior
		when(objectMapper.writeValueAsString(any())).thenReturn("jsonString");

		// Call the commence method
		authEntryPointJwt.commence(request, response, authException);

		// Verify that the response was set correctly
		assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
		verify(objectMapper).writeValue(response.getOutputStream(), expectedBody);
	}
}