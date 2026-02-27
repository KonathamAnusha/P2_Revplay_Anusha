package com.rev.service;

import com.rev.dto.UserDTO;
import com.rev.entity.UserAccount;
import com.rev.mapper.UserMapper;
import com.rev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserServiceInterface {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ================= REGISTRATION =================
    @Override
    public UserAccount registerUser(UserDTO dto) {
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        UserAccount user = userMapper.toEntity(dto);

        // Encode password
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        return userRepository.save(user);
    }




        @Override
        public UserAccount login(String email, String password) {

            UserAccount user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!passwordEncoder.matches(password, user.getPasswordHash())) {
                throw new RuntimeException("Invalid password");
            }

            return user;
        }


    // ================= READ =================
    @Override
    public List<UserAccount> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<UserAccount> getUsersByRole(String role) {
        return userRepository.findByRole(role);
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

    // ================= UPDATE =================
    @Override
    public UserAccount updateUser(Long id, UserDTO dto) {
        UserAccount user = getUserById(id);

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userMapper.updateEntity(user, dto);
        return userRepository.save(user);
    }

    @Override
    public UserAccount changeUserRole(Long id, String role) {
        UserAccount user = getUserById(id);
        user.setRole(role);
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
        userRepository.deleteById(id);
    }

    @Override
    public void deleteUserByEmail(String email) {
        UserAccount user = getUserByEmail(email);
        userRepository.delete(user);
    }
}