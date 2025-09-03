package com.org.ElearnigProject.repository;

import com.org.ElearnigProject.Model.User;
import com.org.ElearnigProject.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role); // <-- ajouter cette ligne

    boolean existsByEmail(String email);
}
