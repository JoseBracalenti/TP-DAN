package isi.dan.msclientes.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import isi.dan.msclientes.dto.CreateUserDTO;
import isi.dan.msclientes.dto.LoginDTO;
import isi.dan.msclientes.exception.ClienteNotFoundException;
import isi.dan.msclientes.exception.InvalidCredentialsException;
import isi.dan.msclientes.exception.UserNotFoundException;
import isi.dan.msclientes.model.User;
import isi.dan.msclientes.servicios.UserService;

@WebMvcTest(UserController.class)
public class UserControllerTestController {
     @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;
    private CreateUserDTO createUserDTO;
    private LoginDTO loginDTO;

    @BeforeEach
    void setUp() {
        // Initialize test data
        user = new User();
        user.setId(1);
        user.setCorreoElectronico("test@example.com");
        user.setPassword("password123");
        user.setNombre("Test User");
        user.setDni("12345678");

        createUserDTO = new CreateUserDTO();
        createUserDTO.setEmail("test@example.com");
        createUserDTO.setPassword("password123");
        createUserDTO.setClienteId(1);
        createUserDTO.setNombre("Test User");

        loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("password123");

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testLogIn_Success() throws Exception {
        when(userService.authenticate(anyString(), anyString())).thenReturn(user);

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("Test User"));

        verify(userService, times(1)).authenticate("test@example.com", "password123");
    }

    @Test
    void testLogIn_InvalidCredentials() throws Exception {
        when(userService.authenticate(anyString(), anyString())).thenThrow(InvalidCredentialsException.class);

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\",\"password\":\"wrongPassword\"}"))
                .andExpect(status().isUnauthorized());

        verify(userService, times(1)).authenticate("test@example.com", "wrongPassword");
    }

    @Test
    void testLogIn_UserNotFound() throws Exception {
        when(userService.authenticate(anyString(), anyString())).thenThrow(UserNotFoundException.class);

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"nonexistent@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).authenticate("nonexistent@example.com", "password123");
    }

    @Test
    void testRegister_Success() throws Exception {
        when(userService.save(any(CreateUserDTO.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\",\"password\":\"password123\",\"name\":\"Test User\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("Test User"));

        verify(userService, times(1)).save(any(CreateUserDTO.class));
    }

    @Test
    void testRegister_ClientNotFound() throws Exception {
        when(userService.save(any(CreateUserDTO.class))).thenThrow(ClienteNotFoundException.class);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\",\"password\":\"password123\",\"name\":\"Test User\"}"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).save(any(CreateUserDTO.class));
    }
}
