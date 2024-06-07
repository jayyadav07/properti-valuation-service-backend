package mu.mcb.property.evalution.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import mu.mcb.property.evalution.dto.JwtResponse;
import mu.mcb.property.evalution.dto.LoginRequest;
import mu.mcb.property.evalution.dto.TokenRefreshRequest;
import mu.mcb.property.evalution.dto.UserRequestDTO;
import mu.mcb.property.evalution.service.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @MockBean
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testRegisterUser() throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        // Set the necessary fields for userRequestDTO
        when(userService.registerUser(any(UserRequestDTO.class))).thenReturn(1L);
        mockMvc.perform(post("/api/v1/auth/registerUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userRequestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAuthenticateUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("password");

        JwtResponse jwtResponse = new JwtResponse();
        // Set the necessary fields for jwtResponse

        when(userService.authenticateUser(any(LoginRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogoutUser() throws Exception {
        mockMvc.perform(get("/api/v1/auth/logout"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRefreshToken() throws Exception {
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest();
        // Set the necessary fields for tokenRefreshRequest
        JwtResponse jwtResponse = new JwtResponse();
        // Set the necessary fields for jwtResponse
        when(userService.refreshToken(any(TokenRefreshRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/api/v1/auth/refreshtoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tokenRefreshRequest)))
                .andExpect(status().isOk());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}