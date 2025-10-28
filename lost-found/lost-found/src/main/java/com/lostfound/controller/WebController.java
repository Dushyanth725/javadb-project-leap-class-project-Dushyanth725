package com.lostfound.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lostfound.model.Item;
import com.lostfound.model.User;
import com.lostfound.service.ItemService;
import com.lostfound.service.UserService;

@Controller
public class WebController {
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            List<Item> allItems = itemService.getAllItems();
            List<Item> lostItems = itemService.getLostItems();
            List<Item> foundItems = itemService.getFoundItems();
            
            model.addAttribute("items", allItems);
            model.addAttribute("lostItems", lostItems);
            model.addAttribute("foundItems", foundItems);
            model.addAttribute("totalItems", allItems.size());
            model.addAttribute("users", userService.getAllUsers());
            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/items")
    public String viewItems(Model model) {
        try {
            model.addAttribute("items", itemService.getAllItems());
            model.addAttribute("users", userService.getAllUsers());
            return "items";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/report")
    public String reportForm(Model model) {
        try {
            model.addAttribute("item", new Item());
            model.addAttribute("users", userService.getAllUsers());
            return "report";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
    
    @PostMapping("/report")
    public String reportItem(@ModelAttribute Item item, @RequestParam Long reportedByUserId, 
                           RedirectAttributes redirectAttributes) {
        try {
            itemService.reportItem(item, reportedByUserId);
            redirectAttributes.addFlashAttribute("message", "Item reported successfully!");
            return "redirect:/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/report";
        }
    }
    
    @GetMapping("/items/{id}")
    public String viewItem(@PathVariable Long id, Model model) {
        try {
            Item item = itemService.getItemById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
            model.addAttribute("item", item);
            model.addAttribute("users", userService.getAllUsers());
            return "item-details";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
    
    @PostMapping("/items/{id}/update")
    public String updateStatus(@PathVariable Long id, 
                             @RequestParam String status,
                             @RequestParam Long userId,
                             @RequestParam(required = false) String remarks,
                             RedirectAttributes redirectAttributes) {
        try {
            itemService.updateItemStatus(id, status, userId, remarks);
            redirectAttributes.addFlashAttribute("message", "Item status updated successfully!");
            return "redirect:/items/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/items/" + id;
        }
    }
}