package com.example.auth.controller;

import com.example.auth.dto.PermissionDto;
import com.example.auth.dto.PermissionParam;
import com.example.auth.dto.BaseResponse;
import com.example.auth.dto.ResponseEnum;
import com.example.auth.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth-management/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<BaseResponse> createPermission(@RequestBody PermissionParam param) {
        PermissionDto permissionDto = permissionService.createPermission(param);
        return new ResponseEntity<>(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Permission created successfully")
                .permissionDto(permissionDto)
                .build(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAllPermissions() {
        List<PermissionDto> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Permissions retrieved successfully")
                .permissionDtos(permissions)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getPermissionById(@PathVariable Long id) {
        PermissionDto permissionDto = permissionService.getPermissionById(id);
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Permission retrieved successfully")
                .permissionDto(permissionDto)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Permission deleted successfully")
                .build());
    }
}
