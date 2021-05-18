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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        final ResponseEntity<User> response = this.userController.createUser(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals("yacadet", user.getUsername());
        assertEquals("1h2hg3hg4gf", user.getPassword());
    }

    @Test
    public void get_user_by_id(){
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("yacadet");
        request.setPassword("#admin123");
        request.setConfirmPassword("#admin123");
        final ResponseEntity<User> response = this.userController.createUser(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        final ResponseEntity<User> response1 = this.userController.findById(0L);
        User userById = response1.getBody();
        assertNotNull(userById);
        assertEquals(user.getId(), userById.getId());
        assertEquals(user.getUsername(), userById.getUsername());
    }

    @Test
    public void get_user_by_username(){
        when(encoder.encode("#admin123")).thenReturn("1h2hg3hg4gf");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("yacadet");
        request.setPassword("#admin123");
        request.setConfirmPassword("#admin123");
        ResponseEntity<User> response = this.userController.createUser(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        System.out.println("user : " + user.getUsername() + "(" + user.getId() + ")");
        response = this.userController.findByUserName(user.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User userFound = response.getBody();
        assertNotNull(userFound);
        System.out.println("user found: " + userFound.getUsername() + "(" + userFound.getId() + ")");
//        assertEquals(user.getId(), userById.getId());
//        assertEquals(user.getUsername(), userById.getUsername());
    }
}
