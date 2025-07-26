package com.org.ElearnigProject.services.impl;

import com.org.ElearnigProject.Model.User;
import com.org.ElearnigProject.dto.userdto;
import com.org.ElearnigProject.mapper.usermappaer;
import com.org.ElearnigProject.repository.UserRepository;
import com.org.ElearnigProject.services.userservices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class userservicesImpl implements userservices {

    private final UserRepository userRepository;

    @Override
    public List<userdto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(usermappaer::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public userdto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return usermappaer.mapToDTO(user);
    }

    @Override
    @Transactional
    public userdto updateUser(UUID id, userdto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        // Map DTO to entity, preserving existing user's ID and tokens
        User updatedUser = usermappaer.mapToEntity(userDto, existingUser);
        
        // Save updated user
        User savedUser = userRepository.save(updatedUser);
        return usermappaer.mapToDTO(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public userdto blockUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        // In a real application, you would set a flag to indicate the user is blocked
        // For now, we'll just return the user
        return usermappaer.mapToDTO(user);
    }

    @Override
    @Transactional
    public userdto unblockUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        // In a real application, you would clear the flag that indicates the user is blocked
        // For now, we'll just return the user
        return usermappaer.mapToDTO(user);
    }
}