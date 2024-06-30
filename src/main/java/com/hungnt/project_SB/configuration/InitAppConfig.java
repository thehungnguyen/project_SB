package com.hungnt.project_SB.configuration;

import com.hungnt.project_SB.emuns.Role;
import com.hungnt.project_SB.entity.User;
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
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()){
                User user = new User();

                user.setUsername("admin");
                user.setPassword("admin@");

                HashSet<String> roles = new HashSet<>();
                roles.add(Role.ADMIN.name());
                user.setRoles(roles);

                userRepository.save(user);
            }
        };
    }
}
