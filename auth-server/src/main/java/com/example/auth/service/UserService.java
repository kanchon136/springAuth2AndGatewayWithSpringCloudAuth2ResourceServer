package com.example.auth.service;

import com.example.auth.dto.UserParam;
import com.example.auth.dto.UserDto;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserDto createUser(UserParam param);

    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();

    void deleteUser(Long id);

    UserDto assignRolesToUser(Long userId, Set<Long> roleIds);

    UserDto removeRoleFromUser(Long userId, Long roleId);

    UserDto getUserWithFullTree(Long id);
}
