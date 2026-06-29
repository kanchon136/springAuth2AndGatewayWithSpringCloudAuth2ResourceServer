package com.example.auth.service;

import com.example.auth.dto.UserResponseDTO;
import com.example.common.model.Page;
import com.example.common.model.Permission;
import com.example.common.model.Role;
import com.example.common.model.User;

import com.example.common.model.Module;

import java.util.List;
import java.util.Set;

public interface AuthService {

    // --- User CRUD & Assignment ---
    User createUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    void deleteUser(Long id);
    void assignRolesToUser(Long userId, Set<Long> roleIds);

    // --- Role CRUD & Assignment ---
    Role createRole(Role role);
    Role getRoleById(Long id);
    List<Role> getAllRoles();
    void deleteRole(Long id);
    void assignModulesToRole(Long roleId, Set<Long> moduleIds);

    // --- Module CRUD & Assignment ---
    Module createModule(Module module);
    Module getModuleById(Long id);
    List<Module> getAllModules();
    void deleteModule(Long id);
    void assignPagesToModule(Long moduleId, Set<Long> pageIds);

    // --- Page CRUD & Assignment ---
    Page createPage(Page page);
    Page getPageById(Long id);
    List<Page> getAllPages();
    void deletePage(Long id);
    void assignPermissionsToPage(Long pageId, Set<Long> permissionIds);

    // --- Permission CRUD ---
    Permission createPermission(Permission permission);
    Permission getPermissionById(Long id);
    List<Permission> getAllPermissions();
    void deletePermission(Long id);

    UserResponseDTO getUserWithFullTree(Long id);
}
