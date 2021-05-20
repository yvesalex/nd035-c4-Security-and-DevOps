package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);

        User user = new User();
        user.setUsername("yacadet");
        user.setPassword("admin123");
        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);
        cart.addItem(new Item(1L,"Spaghetti", new BigDecimal(9.89), "Multiple long lines"));
        UserOrder order = new UserOrder();
        order.setUser(user);
        order.setId(1L);
        order.setItems(cart.getItems());

        when(userRepository.findByUsername("yacadet")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(order));
    }

    @Test
    public void submit(){
        ResponseEntity<UserOrder> response = orderController.submit("yacadet");
        assertNotNull(response);
        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals("yacadet", order.getUser().getUsername());
        assertEquals(1, order.getItems().size());
    }

    @Test
    public void get_for_user(){
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("yacadet");
        assertNotNull(response);
        UserOrder order = response.getBody().get(0);
        System.out.println(response.getStatusCode());
        assertNotNull(order);
        assertEquals("yacadet", order.getUser().getUsername());
        assertEquals(1, order.getItems().size());
    }
}
