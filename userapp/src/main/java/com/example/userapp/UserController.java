
package com.example.userapp;

import com.example.userapp.dto.UserRequestDTO;
import com.example.userapp.dto.UserResponseDTO;
import com.example.userapp.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Create user
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserRequestDTO dto) {

        UserResponseDTO savedUser = userService.createUser(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedUser);
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO dto) {

        return ResponseEntity.ok(
                userService.updateUser(id, dto));
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    // Search users by name
    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDTO>> searchUsersByName(
            @RequestParam String name) {

        return ResponseEntity.ok(
                userService.searchUsersByName(name));
    }

    // Find users by email
    @GetMapping("/email")
    public ResponseEntity<List<UserResponseDTO>> getUsersByEmail(
            @RequestParam String email) {

        return ResponseEntity.ok(
                userService.getUsersByEmail(email));
    }


    @GetMapping("/page")
    public ResponseEntity<Page<UserResponseDTO>> getUsersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page must be >= 0 and size must be between 1 and 100");
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                userService.getUsersPaginated(pageable));
    }

}
