package com.hungnt.project_SB.configuration;


import com.hungnt.project_SB.entity.Role;
import com.hungnt.project_SB.entity.User;
import com.hungnt.project_SB.repository.RoleRepository;
import com.hungnt.project_SB.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
public class InitAppConfig {
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    // se duoc goi moi khi start app de kiem tra
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository){
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()){
                // Tao role ADMIN
                Role role = new Role();

                role.setName("ADMIN");
                role.setDescription("Admin role");

                var roleAdmin = roleRepository.save(role);

                // Tao user AMIN
                User user = new User();

                user.setUsername("admin");
                user.setPassword("admin123");

                var roles = new HashSet<Role>();
                roles.add(roleAdmin);
                user.setRoles(roles);

                userRepository.save(user);
            }
        };
    }
}
