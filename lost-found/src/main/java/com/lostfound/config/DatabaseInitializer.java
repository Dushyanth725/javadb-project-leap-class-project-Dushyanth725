package com.lostfound.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.lostfound.model.User;
import com.lostfound.repository.UserRepository;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only add default users if the database is empty
        if (userRepository.count() == 0) {
            // Create some default users
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@campus.edu");
            admin.setContact("123-456-7890");
            admin.setRole("ADMIN");
            userRepository.save(admin);

            User student1 = new User();
            student1.setName("John Doe");
            student1.setEmail("john.doe@campus.edu");
            student1.setContact("123-456-7891");
            student1.setRole("USER");
            userRepository.save(student1);

            User student2 = new User();
            student2.setName("Jane Smith");
            student2.setEmail("jane.smith@campus.edu");
            student2.setContact("123-456-7892");
            student2.setRole("USER");
            userRepository.save(student2);

            System.out.println("Database initialized with default users");
        }
    }
}