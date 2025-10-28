package com.lostfound.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lostfound.model.Item;
import com.lostfound.model.SearchParams;
import com.lostfound.model.User;
import com.lostfound.service.ItemService;
import com.lostfound.service.UserService;

@Controller
@RequestMapping("/web")
public class WebController {
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user, RedirectAttributes redirectAttributes) {
        try {
            user.setRole("USER");
            userService.createUser(user);
            redirectAttributes.addFlashAttribute("message", "Registration successful! You can now report items.");
            return "redirect:/web/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/register";
        }
    }
    
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

    @GetMapping("/items/lost")
    public String viewLostItems(Model model) {
        try {
            List<Item> lostItems = itemService.getLostItems();
            model.addAttribute("items", lostItems);
            model.addAttribute("status", "LOST");
            return "items-status";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/items/found")
    public String viewFoundItems(Model model) {
        try {
            List<Item> foundItems = itemService.getFoundItems();
            model.addAttribute("items", foundItems);
            model.addAttribute("status", "FOUND");
            return "items-status";
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
            return "report-item";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
    
    @PostMapping("/report")
    public String reportItem(@ModelAttribute Item item, @RequestParam Long reportedByUserId, 
                           RedirectAttributes redirectAttributes, Model model) {
        try {
            if (reportedByUserId == null) {
                model.addAttribute("error", "Please select a reporter");
                model.addAttribute("users", userService.getAllUsers());
                return "report-item";
            }

            itemService.reportItem(item, reportedByUserId);
            redirectAttributes.addFlashAttribute("message", "Item reported successfully!");
            return "redirect:/web/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/report";
        }
    }
    
    @GetMapping("/item/{id}")
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
    public String updateItemStatus(@PathVariable Long id,
                                 @RequestParam String status,
                                 @RequestParam Long userId,
                                 @RequestParam(required = false) String remarks,
                                 RedirectAttributes redirectAttributes) {
        try {
            itemService.updateItemStatus(id, status, userId, remarks);
            redirectAttributes.addFlashAttribute("message", "Item status updated successfully!");
            return "redirect:/web/item/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/item/" + id;
        }
    }
    
    @GetMapping("/search")
    public String searchItems(@RequestParam(required = false) String category,
                            @RequestParam(required = false) String location,
                            @RequestParam(required = false) String name,
                            Model model) {
        try {
            List<Item> items = itemService.searchItems(category, location, name);
            model.addAttribute("items", items);
            model.addAttribute("searchParams", new SearchParams(category, location, name));
            return "search";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}