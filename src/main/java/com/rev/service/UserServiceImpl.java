package com.rev.service;

import com.rev.dto.UserDTO;
import com.rev.entity.UserAccount;
import com.rev.mapper.UserMapper;
import com.rev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserServiceInterface {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    // ================= REGISTRATION =================
    @Override
    public UserAccount registerUser(UserDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        UserAccount user = new UserAccount();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setDisplayName(dto.getDisplayName());
        user.setBio(dto.getBio());
        user.setProfilePicture(dto.getProfilePicture());



        // Role mapping
        user.setRole(UserAccount.Role.valueOf(dto.getRole().toUpperCase()));
        user.setStatus("ACTIVE");

        return userRepository.save(user);
    }

    @Override
    public UserAccount login(String email, String password) {
        UserAccount user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }

    // ================= READ =================
    @Override
    public List<UserAccount> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserAccount getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserAccount getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<UserAccount> getUsersByRole(String role) {
        UserAccount.Role r = UserAccount.Role.valueOf(role.toUpperCase());
        return userRepository.findByRole(r);
    }

    // ================= UPDATE =================
    @Override
    public UserAccount updateUser(Long id, UserDTO dto) {
        UserAccount user = getUserById(id);

        if (dto.getFullName() != null) user.setFullName(dto.getFullName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPassword() != null) user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        if (dto.getDisplayName() != null) user.setDisplayName(dto.getDisplayName());
        if (dto.getBio() != null) user.setBio(dto.getBio());
        if (dto.getProfilePicture() != null) user.setProfilePicture(dto.getProfilePicture());

        return userRepository.save(user);
    }

    @Override
    public UserAccount changeUserRole(Long id, String role) {
        UserAccount user = getUserById(id);
        user.setRole(UserAccount.Role.valueOf(role.toUpperCase()));
        return userRepository.save(user);
    }

    @Override
    public UserAccount changeUserStatus(Long id, String status) {
        UserAccount user = getUserById(id);
        user.setStatus(status);
        return userRepository.save(user);
    }

    // ================= DELETE =================
    @Override
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public void deleteUserByEmail(String email) {
        UserAccount user = getUserByEmail(email);
        userRepository.delete(user);
    }

    // ================= SPRING SECURITY =================
    @Override
    public UserDetails loadUserByUsername(String email) {
        UserAccount user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}