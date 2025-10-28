package com.lostfound.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lostfound.model.Item;
import com.lostfound.model.ItemLog;
import com.lostfound.model.User;
import com.lostfound.repository.ItemLogRepository;
import com.lostfound.repository.ItemRepository;
import com.lostfound.repository.UserRepository;

@Service
@Transactional
public class ItemService {
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ItemLogRepository itemLogRepository;
    
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
    
    public List<Item> getLostItems() {
        return itemRepository.findByStatus("LOST");
    }
    
    public List<Item> getFoundItems() {
        return itemRepository.findByStatus("FOUND");
    }
    
    public Item reportItem(Item item, Long reportedByUserId) {
        User user = userRepository.findById(reportedByUserId)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        item.setReportedBy(user);
        item = itemRepository.save(item);
        
        String action = item.getStatus().equals("FOUND") ? "ITEM_FOUND" : "ITEM_LOST";
        createLog(item, user, action, "Item " + action.toLowerCase());
        
        return item;
    }
    
    public Item updateItemStatus(Long itemId, String status, Long userId, String remarks) {
        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Item not found"));
            
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        item.setStatus(status);
        
        if (status.equals("RECOVERED")) {
            item.setResolvedAt(LocalDateTime.now());
        }
        
        item.setRemarks(remarks);
        item = itemRepository.save(item);
        
        createLog(item, user, "STATUS_UPDATED", 
            "Status updated to " + status + " by " + user.getName());
            
        return item;
    }
    
    private void createLog(Item item, User performedBy, String action, String remarks) {
        ItemLog log = new ItemLog();
        log.setItem(item);
        log.setPerformedBy(performedBy);
        log.setAction(action);
        log.setRemarks(remarks);
        itemLogRepository.save(log);
    }
    
    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }
    
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
    
    public List<Item> searchItems(String category, String location, String name) {
        List<Item> items = itemRepository.findAll();
        
        if (category != null && !category.isEmpty()) {
            items = items.stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        }
        
        if (location != null && !location.isEmpty()) {
            items = items.stream()
                .filter(item -> item.getLocation().toLowerCase().contains(location.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (name != null && !name.isEmpty()) {
            items = items.stream()
                .filter(item -> item.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        return items;
    }
}