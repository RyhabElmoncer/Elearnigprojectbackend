package com.org.ElearnigProject.services;

import com.org.ElearnigProject.dto.userdto;

import java.util.List;
import java.util.UUID;

public interface userservices {
    List<userdto> getAllUsers();
    
    userdto getUserById(UUID id);
    
    userdto updateUser(UUID id, userdto userDto);
    
    void deleteUser(UUID id);
    
    userdto blockUser(UUID id);
    
    userdto unblockUser(UUID id);
}
