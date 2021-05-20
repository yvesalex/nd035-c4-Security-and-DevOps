package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private User user;
    private Cart cart;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
    }

    @Before
    public void setUpUserAndCart(){
        user = new User();
        user.setUsername("yacadet");
        user.setPassword("admin123");

        cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);
        when(userRepository.findById(0L)).thenReturn(java.util.Optional.of(user));
    }

    @Before
    public void setUpItems(){
        List<Item> items = new ArrayList<>();
        items.add(new Item(1L,"Spaghetti", new BigDecimal(9.89), "Multiple long lines"));
        items.add(new Item(2L,"Sandwich", new BigDecimal(5.76), "Enriched bread with tomato sauce"));
        items.add(new Item(3L,"Apple", new BigDecimal(1.25), "Fresh fruit from Haiti"));

        when(userRepository.findByUsername("yacadet")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(items.get(0)));
    }

    @Test
    public void add_to_cart(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("yacadet");
        request.setItemId(1L);
        request.setQuantity(2);
        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response);
        Cart obj = response.getBody();
        assertNotNull(obj);
        assertEquals(new BigDecimal(9.89), obj.getItems().get(0).getPrice());
        assertEquals("Spaghetti", obj.getItems().get(0).getName());
        assertEquals("yacadet", obj.getUser().getUsername());
    }

    @Test
    public void remove_from_cart(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("yacadet");
        request.setItemId(1L);
        request.setQuantity(2);
        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response);
        Cart obj = response.getBody();
        assertNotNull(obj);
        assertEquals(0, obj.getItems().size());
    }
}
