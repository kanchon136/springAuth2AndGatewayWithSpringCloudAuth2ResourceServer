package com.example.auth.impl;

import com.example.auth.dto.UserParam;
import com.example.auth.dto.UserDto;
import com.example.auth.mapper.EntityMapper;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.UserService;
import com.example.common.model.Role;
import com.example.common.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDto createUser(UserParam param) {
        if (userRepository.existsByUsername(param.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + param.getUsername());
        }
        if (userRepository.existsByEmail(param.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + param.getEmail());
        }

        User user = new User();
        user.setUsername(param.getUsername());
        user.setPassword(passwordEncoder.encode(param.getPassword()));
        user.setEmail(param.getEmail());
        user.setEnabled(true);

        Set<Role> roles = new HashSet<>();
        if (param.getRoleIds() != null && !param.getRoleIds().isEmpty()) {
            roles = param.getRoleIds().stream()
                    .map(roleId -> roleRepository.findById(roleId)
                            .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId)))
                    .collect(Collectors.toSet());
        }
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        return EntityMapper.toUserDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return EntityMapper.toUserDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(EntityMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UserDto assignRolesToUser(Long userId, Set<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        List<Role> roles = roleRepository.findAllById(roleIds);
        user.setRoles(new HashSet<>(roles));
        User savedUser = userRepository.save(user);
        return EntityMapper.toUserDto(savedUser);
    }

    @Override
    @Transactional
    public UserDto removeRoleFromUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));
        user.getRoles().remove(role);
        User savedUser = userRepository.save(user);
        return EntityMapper.toUserDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserWithFullTree(Long id) {
        User user = userRepository.findUserWithAllPermissions(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return EntityMapper.toUserDto(user);
    }
}
