package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.security.UserDetailsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
    private UserDetailsService userDetailsService = mock(UserDetailsServiceImpl.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObject(userController,"userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
        TestUtils.injectObject(userController, "userDetailsService", userDetailsService);
    }

    @Test
    public void create_user_happy_path(){
        when(encoder.encode("#admin123")).thenReturn("1h2hg3hg4gf");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("yacadet");
        request.setPassword("#admin123");
        request.setConfirmPassword("#admin123");
        final ResponseEntity<?> response = this.userController.createUser(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = (User) response.getBody();
        assertNotNull(user);
        assertEquals("yacadet", user.getUsername());
        assertEquals("1h2hg3hg4gf", user.getPassword());
    }

    @Test
    public void get_user_by_id(){
        User user = new User();
        user.setUsername("yacadet");
        user.setPassword("admin123");
        when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));
        ResponseEntity<User> userResponseEntity = userController.findById(0L);
        assertTrue(userResponseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("yacadet", userResponseEntity.getBody().getUsername());
        assertEquals("admin123", userResponseEntity.getBody().getPassword());
    }

    @Test
    public void get_user_by_username(){
        User user = new User();
        user.setUsername("yacadet");
        user.setPassword("admin123");
        when(userRepository.findByUsername("yacadet")).thenReturn(user);
        ResponseEntity<User> userResponseEntity = userController.findByUserName("yacadet");
        assertTrue(userResponseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("yacadet", userResponseEntity.getBody().getUsername());
        assertEquals("admin123", userResponseEntity.getBody().getPassword());
    }
}
