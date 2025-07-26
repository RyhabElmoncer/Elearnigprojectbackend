package com.org.ElearnigProject.mapper;

import com.org.ElearnigProject.Model.User;
import com.org.ElearnigProject.dto.userdto;

public class usermappaer {
    
    public static userdto mapToDTO(User user) {
        if (user == null) {
            return null;
        }
        
        return userdto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .registrationDate(user.getRegistrationDate())
                .isActive(user.isEnabled())
                .build();
    }
    
    public static User mapToEntity(userdto dto, User existingUser) {
        if (dto == null) {
            return null;
        }
        
        // If we're updating an existing user, start with that as the base
        User.UserBuilder builder = existingUser != null ? 
                User.builder()
                    .id(existingUser.getId())
                    .tokens(existingUser.getTokens()) : 
                User.builder();
        
        // Update fields from DTO
        return builder
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .role(dto.getRole())
                .registrationDate(dto.getRegistrationDate())
                .build();
    }
    
    // Convenience method for creating a new user from DTO
    public static User mapToEntity(userdto dto) {
        return mapToEntity(dto, null);
    }
}
