package com.khaleel.objectstorage.config;

import com.khaleel.objectstorage.model.User;
import com.khaleel.objectstorage.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

//    public DataSeeder(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception{
        if(userRepository.count() == 0){
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("password"))
                    .email("admin@example.com")
                    .build();
            userRepository.save(admin);
            System.out.println("DB seeder: Created default user admin with ID 1");
        }
    }
}
