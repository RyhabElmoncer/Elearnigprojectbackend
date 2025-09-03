package com.org.ElearnigProject.config;

import com.org.ElearnigProject.Model.User;
import com.org.ElearnigProject.enums.Role;
import com.org.ElearnigProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Vérifie si un admin existe déjà
        if (userRepository.findByRole(Role.ADMIN).isEmpty()) {
            User admin = User.builder()
                    .firstName("Admin")
                    .lastName("Super")
                    .email("admin@chiheb.com")
                    .password(passwordEncoder.encode("Admin123!")) // mot de passe sécurisé
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            System.out.println("Admin créé : admin@televora.com / Admin123!");
        } else {
            System.out.println("Admin déjà existant.");
        }
    }
}
