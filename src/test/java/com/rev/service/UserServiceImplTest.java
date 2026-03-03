package com.rev.service;

import com.rev.dto.ArtistDTO;
import com.rev.dto.UserDTO;
import com.rev.entity.ArtistProfile;
import com.rev.entity.UserAccount;
import com.rev.entity.UserAccount.Role;
import com.rev.mapper.ArtistMapper;
import com.rev.mapper.UserMapper;
import com.rev.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ArtistMapper artistMapper;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    private UserAccount testUser;
    private UserDTO testDTO;

    @BeforeEach
    void setUp() {
        testUser = new UserAccount();
        testUser.setUserId(1L);
        testUser.setFullName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash("encodedPass");
        testUser.setRole(Role.LISTENER);
        testUser.setStatus("ACTIVE");

        testDTO = new UserDTO();
        testDTO.setFullName("Test User");
        testDTO.setEmail("test@example.com");
        testDTO.setPassword("password123");
        testDTO.setRole(Role.LISTENER);
    }

    @Test
    void registerUser_Success() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPass");
        when(userRepository.save(any(UserAccount.class))).thenReturn(testUser);

        UserAccount result = userService.registerUser(testDTO);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).save(any(UserAccount.class));
    }

    @Test
    void registerArtistUser_WithProfile_Success() {
        // Setup artist DTO
        ArtistDTO artistDTO = new ArtistDTO();
        artistDTO.setStageName("DJ Test");
        artistDTO.setGenre("Electronic");

        UserDTO artistUserDTO = new UserDTO();
        artistUserDTO.setFullName("Artist User");
        artistUserDTO.setEmail("artist@example.com");
        artistUserDTO.setPassword("password123");
        artistUserDTO.setRole(Role.ARTIST);
        artistUserDTO.setArtistProfile(artistDTO);

        UserAccount artistUser = new UserAccount();
        artistUser.setUserId(2L);
        artistUser.setEmail("artist@example.com");
        artistUser.setRole(Role.ARTIST);

        ArtistProfile profile = new ArtistProfile();
        profile.setStageName("DJ Test");

        when(userRepository.existsByEmail("artist@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPass");
        when(artistMapper.toEntity(eq(artistDTO), any(UserAccount.class))).thenReturn(profile);
        when(userRepository.save(any(UserAccount.class))).thenReturn(artistUser);

        UserAccount result = userService.registerUser(artistUserDTO);

        assertNotNull(result);
        assertEquals(Role.ARTIST, result.getRole());
        verify(artistMapper).toEntity(eq(artistDTO), any(UserAccount.class));
        verify(userRepository).save(any(UserAccount.class));
    }

    @Test
    void registerUser_EmailExists() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        assertThrows(RuntimeException.class, () -> userService.registerUser(testDTO));
    }

    @Test
    void login_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPass")).thenReturn(true);

        UserAccount result = userService.login("test@example.com", "password123");
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void login_InvalidPassword() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.login("test@example.com", "wrongPass"));
    }

    @Test
    void login_UserNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.login("notfound@example.com", "pass"));
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        UserAccount result = userService.getUserById(1L);
        assertEquals(1L, result.getUserId());
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getUserById(999L));
    }

    @Test
    void getUserByEmail_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        UserAccount result = userService.getUserByEmail("test@example.com");
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));
        List<UserAccount> result = userService.getAllUsers();
        assertEquals(1, result.size());
    }

    @Test
    void updateUser_Success() {
        UserDTO updateDTO = new UserDTO();
        updateDTO.setDisplayName("Updated Name");
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(UserAccount.class))).thenReturn(testUser);

        UserAccount result = userService.updateUser(1L, updateDTO);
        assertNotNull(result);
    }

    @Test
    void deleteUserById_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);
        assertDoesNotThrow(() -> userService.deleteUserById(1L));
    }

    @Test
    void deleteUserById_NotFound() {
        when(userRepository.existsById(999L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> userService.deleteUserById(999L));
    }

    @Test
    void changeUserRole() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(UserAccount.class))).thenReturn(testUser);
        UserAccount result = userService.changeUserRole(1L, "ARTIST");
        assertNotNull(result);
    }

    @Test
    void changeUserStatus() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(UserAccount.class))).thenReturn(testUser);
        UserAccount result = userService.changeUserStatus(1L, "INACTIVE");
        assertNotNull(result);
    }

    @Test
    void loadUserByUsername_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        var result = userService.loadUserByUsername("test@example.com");
        assertNotNull(result);
    }
}
