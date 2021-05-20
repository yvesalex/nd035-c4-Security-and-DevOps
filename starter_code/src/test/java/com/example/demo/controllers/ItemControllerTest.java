package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
        List<Item> items = new ArrayList<>();
        items.add(new Item(1L,"Spaghetti", new BigDecimal(9.89), "Multiple long lines"));
        items.add(new Item(2L,"Sandwich", new BigDecimal(5.76), "Enriched bread with tomato sauce"));
        items.add(new Item(3L,"Apple", new BigDecimal(1.25), "Fresh fruit from Haiti"));

        when(itemRepository.findAll()).thenReturn(items);
        when(itemRepository.findByName("Spaghetti")).thenReturn(Arrays.asList(items.get(0)));
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(items.get(0)));
    }

    @Test
    public void get_all(){
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(3, items.size());
    }

    @Test
    public void get_by_id(){
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        Item item = response.getBody();
        assertNotNull(item);
        assertEquals("Spaghetti", item.getName());
        assertEquals(new BigDecimal(9.89), item.getPrice());
    }
    @Test
    public void get_by_name(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Spaghetti");
        assertNotNull(response);
        Item item = response.getBody().get(0);
        assertNotNull(item);
        assertEquals("Spaghetti", item.getName());
        assertEquals(new BigDecimal(9.89), item.getPrice());
    }
}
