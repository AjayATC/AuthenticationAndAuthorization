
package com.example.userapp.service;

import com.example.userapp.dto.UserRequestDTO;
import com.example.userapp.dto.UserResponseDTO;
import com.example.userapp.UserNotFoundException;
import com.example.userapp.User;
import com.example.userapp.UserRepository;
import com.example.userapp.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    // Test 1: Successfully create a user
    @Test
    void shouldCreateUser() {

        UserRequestDTO dto = new UserRequestDTO();
        dto.setName("John");
        dto.setEmail("john@gmail.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("John");
        savedUser.setEmail("john@gmail.com");

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        UserResponseDTO response = userService.createUser(dto);

        assertEquals(1L, response.getId());
        assertEquals("John", response.getName());
        assertEquals("john@gmail.com", response.getEmail());

        verify(userRepository, times(1))
                .save(any(User.class));
    }

    // Test 2: Successfully find a user
    @Test
    void shouldReturnUserById() {

        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@gmail.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserResponseDTO response = userService.getUserById(1L);

        assertEquals("John", response.getName());
        assertEquals("john@gmail.com", response.getEmail());
    }

    // Test 3: User not found
    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(999L)
        );
    }
}
