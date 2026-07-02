package com.example.auth.mapper;

import com.example.common.model.*;
import com.example.common.model.Module;
import com.example.auth.dto.*;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityMapper {

    public static UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .roles(user.getRoles() == null ? Collections.emptySet()
                        : user.getRoles().stream()
                                .map(EntityMapper::toRoleDto)
                                .collect(Collectors.toSet()))
                .build();
    }

    public static RoleDto toRoleDto(Role role) {
        if (role == null) {
            return null;
        }
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .modules(role.getModules() == null ? Collections.emptySet()
                        : role.getModules().stream()
                                .map(EntityMapper::toModuleDto)
                                .collect(Collectors.toSet()))
                .build();
    }

    public static ModuleDto toModuleDto(Module module) {
        if (module == null) {
            return null;
        }
        return ModuleDto.builder()
                .id(module.getId())
                .name(module.getName())
                .icon(module.getIcon())
                .pages(module.getPages() == null ? Collections.emptySet()
                        : module.getPages().stream()
                                .map(EntityMapper::toPageDto)
                                .collect(Collectors.toSet()))
                .build();
    }

    public static PageDto toPageDto(Page page) {
        if (page == null) {
            return null;
        }
        return PageDto.builder()
                .id(page.getId())
                .name(page.getName())
                .urlPath(page.getUrlPath())
                .permissions(page.getPermissions() == null ? Collections.emptySet()
                        : page.getPermissions().stream()
                                .map(EntityMapper::toPermissionDto)
                                .collect(Collectors.toSet()))
                .build();
    }

    public static PermissionDto toPermissionDto(Permission permission) {
        if (permission == null) {
            return null;
        }
        return PermissionDto.builder()
                .id(permission.getId())
                .name(permission.getName())
                .action(permission.getAction())
                .build();
    }
}
