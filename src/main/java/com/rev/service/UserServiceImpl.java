package com.rev.service;

import com.rev.dto.UserDTO;
import com.rev.entity.ArtistProfile;
import com.rev.entity.UserAccount;
import com.rev.mapper.ArtistMapper;
import com.rev.mapper.UserMapper;
import com.rev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ArtistMapper artistMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    // ================= REGISTRATION =================
    @Override
    public UserAccount registerUser(UserDTO dto) {
        log.info("Registering user with email: {}", dto.getEmail());

        if (userRepository.existsByEmail(dto.getEmail())) {
            log.warn("Registration failed - email already in use: {}", dto.getEmail());
            throw new RuntimeException("Email already in use");
        }

        UserAccount user = new UserAccount();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setDisplayName(dto.getDisplayName());
        user.setBio(dto.getBio());
        user.setProfilePicture(dto.getProfilePicture());

        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }
        user.setStatus("ACTIVE");

        // Create ArtistProfile if registering as ARTIST
        if (user.getRole() == UserAccount.Role.ARTIST && dto.getArtistProfile() != null) {
            ArtistProfile artistProfile = artistMapper.toEntity(dto.getArtistProfile(), user);
            user.setArtistProfile(artistProfile);
            log.info("Creating artist profile for user: {}, stageName: {}",
                    dto.getEmail(), dto.getArtistProfile().getStageName());
        }

        UserAccount saved = userRepository.save(user);
        log.info("User registered successfully with id: {}, role: {}", saved.getUserId(), saved.getRole());
        return saved;
    }

    @Override
    public UserAccount login(String email, String password) {
        log.info("Login attempt for email: {}", email);

        UserAccount user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Login failed - user not found: {}", email);
                    return new RuntimeException("Invalid email or password");
                });

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            log.warn("Login failed - invalid password for: {}", email);
            throw new RuntimeException("Invalid email or password");
        }

        log.info("User logged in successfully: {}, role: {}", email, user.getRole());
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

        if (dto.getFullName() != null)
            user.setFullName(dto.getFullName());
        if (dto.getEmail() != null)
            user.setEmail(dto.getEmail());
        if (dto.getPassword() != null)
            user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        if (dto.getDisplayName() != null)
            user.setDisplayName(dto.getDisplayName());
        if (dto.getBio() != null)
            user.setBio(dto.getBio());
        if (dto.getProfilePicture() != null)
            user.setProfilePicture(dto.getProfilePicture());

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
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
    }
}