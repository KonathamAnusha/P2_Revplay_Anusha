package com.rev.service;

import com.rev.dto.UserDTO;
import com.rev.dto.UserStatsDTO;
import com.rev.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserServiceInterface {

    // ================= REGISTRATION =================
    UserAccount registerUser(UserDTO dto);
    UserAccount login(String email, String password);

    // ================= READ =================
    List<UserAccount> getAllUsers();

    UserAccount getUserById(Long id);

    UserAccount getUserByEmail(String email);

    List<UserAccount> getUsersByRole(String role);

    // ================= UPDATE =================
    UserAccount updateUser(Long id, UserDTO dto);

    UserAccount changeUserRole(Long id, String role);

    UserAccount changeUserStatus(Long id, String status);

    // ================= DELETE =================
    void deleteUserById(Long id);

    void deleteUserByEmail(String email);



    UserDetails loadUserByUsername(String email);
}

