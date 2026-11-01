package com.example.userapp;

import com.example.userapp.User;
import com.example.userapp.UserRepository;
import com.example.userapp.dto.UserMapper;
import com.example.userapp.dto.UserRequestDTO;
import com.example.userapp.dto.UserResponseDTO;
import com.example.userapp.UserNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    // Constructor injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1. Get all users
    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponseDTO)
                .toList();
    }

    // 2. Get user by ID
    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return UserMapper.toResponseDTO(user);
    }

    // 3. Create user
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {

        logger.info("Creating new user with email: {}", dto.getEmail());

        User user = UserMapper.toEntity(dto);

        User savedUser = userRepository.save(user);

        logger.info("User created successfully with ID: {}", savedUser.getId());

        return UserMapper.toResponseDTO(savedUser);
    }

    // 4. Update user
    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        User updatedUser = userRepository.save(user);

        logger.info("User updated successfully with ID: {}", id);

        return UserMapper.toResponseDTO(updatedUser);
    }

    // 5. Delete user
    @Transactional
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        logger.info("Deleting user with ID: {}", id);

        userRepository.delete(user);

        logger.info("User deleted successfully with ID: {}", id);
    }

    // 6. Search users by name
    public List<UserResponseDTO> searchUsersByName(String name) {

        return userRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(UserMapper::toResponseDTO)
                .toList();
    }

    // 7. Get users by email
    public List<UserResponseDTO> getUsersByEmail(String email) {

        return userRepository.findByEmail(email)
                .stream()
                .map(UserMapper::toResponseDTO)
                .toList();
    }

    // 8. Pagination and sorting
    public Page<UserResponseDTO> getUsersPaginated(Pageable pageable) {

        return userRepository.findAll(pageable)
                .map(UserMapper::toResponseDTO);
    }
}