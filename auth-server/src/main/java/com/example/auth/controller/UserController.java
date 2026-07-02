package com.example.auth.controller;

import com.example.auth.dto.UserDto;
import com.example.auth.dto.UserParam;
import com.example.auth.dto.BaseResponse;
import com.example.auth.dto.ResponseEnum;
import com.example.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<BaseResponse> createUser(@RequestBody UserParam param) {
        UserDto userDto = userService.createUser(param);
        return new ResponseEntity<>(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("User created successfully")
                .userDto(userDto)
                .build(), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("User retrieved successfully")
                .userDto(userDto)
                .build());
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Users retrieved successfully")
                .userDtos(users)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("User deleted successfully")
                .build());
    }

    @PostMapping("/{userId}/assign-roles")
    public ResponseEntity<BaseResponse> assignRolesToUser(
            @PathVariable Long userId,
            @RequestBody Set<Long> roleIds) {
        UserDto userDto = userService.assignRolesToUser(userId, roleIds);
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Roles assigned to user successfully")
                .userDto(userDto)
                .build());
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<BaseResponse> removeRoleFromUser(
            @PathVariable Long userId,
            @PathVariable Long roleId) {
        UserDto userDto = userService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Role removed from user successfully")
                .userDto(userDto)
                .build());
    }

    @GetMapping("/full-tree/{id}")
    public ResponseEntity<BaseResponse> getUserWithFullTree(@PathVariable Long id) {
        UserDto userDto = userService.getUserWithFullTree(id);
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("User tree retrieved successfully")
                .userDto(userDto)
                .build());
    }
}
