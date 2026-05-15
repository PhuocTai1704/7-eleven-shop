package com.APIshop.BEShop.config;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.APIshop.BEShop.entity.Role;
import com.APIshop.BEShop.entity.User;
import com.APIshop.BEShop.repository.RoleRepo;
import com.APIshop.BEShop.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SystemInitializer implements CommandLineRunner {

    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        try {

            Role adminRole = new Role();
            adminRole.setRoleId(AppConstants.ADMIN_ID);
            adminRole.setRoleName("ADMIN");

            Role userRole = new Role();
            userRole.setRoleId(AppConstants.USER_ID);
            userRole.setRoleName("USER");

            List<Role> roles = List.of(userRole, adminRole);

            List<Role> savedRoles = roleRepo.saveAll(roles);

            if (!userRepo.existsByUsername("admin")) {
                User admin = new User();
                admin.setFullName("Admin");
                admin.setUsername("admin");
                String encodedPass = passwordEncoder.encode("adsads");
                admin.setPassword(encodedPass);
                admin.setCreatedAt(LocalDateTime.now());
                admin.getRoles().addAll(roleRepo.findAll());
                userRepo.save(admin);
            }
            savedRoles.forEach(System.out::println);
            System.out.println("username: " + "admin");
            System.out.println("password: " + "adsads");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}