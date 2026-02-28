package com.rev.restcontroller;

import com.rev.dto.UserDTO;
import com.rev.dto.UserStatsDTO;
import com.rev.entity.UserAccount;
import com.rev.mapper.UserMapper;
import com.rev.service.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserServiceInterface userService;
    private final UserMapper userMapper;

    // ------------------- REGISTER USER -------------------
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO dto) {
        UserAccount saved = userService.registerUser(dto);
        return ResponseEntity.ok(userMapper.toDTO(saved));
    }


    // ------------------- LOGIN USER -------------------
    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestParam String emailOrUsername,
                                             @RequestParam String password) {
        UserAccount user = userService.login(emailOrUsername, password);
        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    // ------------------- GET ALL USERS -------------------
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    // ------------------- GET USER BY ID -------------------
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserAccount user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    // ------------------- GET USER BY EMAIL -------------------
    @GetMapping("/email")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        UserAccount user = userService.getUserByEmail(email);
        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    // ------------------- UPDATE USER -------------------
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
                                              @RequestBody UserDTO dto) {
        UserAccount updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(userMapper.toDTO(updated));
    }

    // ------------------- DELETE USER BY ID -------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // ------------------- DELETE USER BY EMAIL -------------------
    @DeleteMapping("/email")
    public ResponseEntity<String> deleteUserByEmail(@RequestParam String email) {
        userService.deleteUserByEmail(email);
        return ResponseEntity.ok("User deleted successfully");
    }



    // ------------------- CHANGE STATUS -------------------
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserDTO> changeStatus(@PathVariable Long id,
                                                @RequestParam String status) {
        UserAccount updated = userService.changeUserStatus(id, status);
        return ResponseEntity.ok(userMapper.toDTO(updated));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<UserStatsDTO> getUserStats(@PathVariable Long id) {
        UserStatsDTO stats = userService.getUserStats(id);
        return ResponseEntity.ok(stats);
    }
}