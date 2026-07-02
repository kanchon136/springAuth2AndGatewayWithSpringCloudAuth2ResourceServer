package com.example.auth.service;

import com.example.auth.dto.RoleParam;
import com.example.auth.dto.RoleDto;

import java.util.List;
import java.util.Set;

public interface RoleService {
    RoleDto createRole(RoleParam param);

    RoleDto getRoleById(Long id);

    List<RoleDto> getAllRoles();

    void deleteRole(Long id);

    RoleDto assignModulesToRole(Long roleId, Set<Long> moduleIds);
}
