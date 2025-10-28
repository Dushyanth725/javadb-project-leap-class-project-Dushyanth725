package com.lostfound.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lostfound.model.Item;
import com.lostfound.service.ItemService;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    
    @Autowired
    private ItemService itemService;
    
    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }
    
    @PostMapping
    public Item reportItem(@RequestBody Item item, @RequestParam Long reportedBy) {
        return itemService.reportItem(item, reportedBy);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Optional<Item> item = itemService.getItemById(id);
        return item.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/status")
    public Item updateItemStatus(@PathVariable Long id, 
                                @RequestParam String status,
                                @RequestParam Long userId,
                                @RequestParam(required = false) String remarks) {
        return itemService.updateItemStatus(id, status, userId, remarks);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/search")
    public List<Item> searchItems(@RequestParam(required = false) String category,
                                 @RequestParam(required = false) String location,
                                 @RequestParam(required = false) String name) {
        return itemService.searchItems(category, location, name);
    }
}