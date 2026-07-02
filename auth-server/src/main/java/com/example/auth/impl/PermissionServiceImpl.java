package com.example.auth.impl;

import com.example.auth.dto.PermissionDto;
import com.example.auth.dto.PermissionParam;
import com.example.auth.mapper.EntityMapper;
import com.example.auth.repository.PermissionRepository;
import com.example.auth.service.PermissionService;
import com.example.common.model.Permission;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional
    public PermissionDto createPermission(PermissionParam param) {
        Permission permission = new Permission();
        permission.setName(param.getName());
        permission.setAction(param.getAction());

        Permission savedPermission = permissionRepository.save(permission);
        return EntityMapper.toPermissionDto(savedPermission);
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionDto getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permission not found with id: " + id));
        return EntityMapper.toPermissionDto(permission);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionDto> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(EntityMapper::toPermissionDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePermission(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new EntityNotFoundException("Permission not found with id: " + id);
        }
        permissionRepository.deleteById(id);
    }
}
