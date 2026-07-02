package com.example.auth.service;

import com.example.auth.dto.PermissionDto;
import com.example.auth.dto.PermissionParam;

import java.util.List;

public interface PermissionService {
    PermissionDto createPermission(PermissionParam param);

    PermissionDto getPermissionById(Long id);

    List<PermissionDto> getAllPermissions();

    void deletePermission(Long id);
}
