package com.example.auth.controller;


import com.example.auth.dto.UserResponseDTO;
import com.example.auth.service.AuthService;
import com.example.common.model.*;
import com.example.common.model.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth-management")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ==========================================
    // 👤 1. USER API'S (CRUD & ASSIGNMENT)
    // ==========================================

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(authService.createUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(authService.getUserById(id));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        authService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // 🔗 ধাপ ১: ইউজারকে রোল অ্যাসাইন করা
    @PostMapping("/users/{userId}/assign-roles")
    public ResponseEntity<Map<String, String>> assignRolesToUser(
            @PathVariable Long userId,
            @RequestBody Set<Long> roleIds) {
        authService.assignRolesToUser(userId, roleIds);
        return ResponseEntity.ok(Map.of("message", "Roles assigned to user successfully"));
    }

    // ==========================================
    // 🎖️ 2. ROLE API'S (CRUD & ASSIGNMENT)
    // ==========================================

    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        return new ResponseEntity<>(authService.createRole(role), HttpStatus.CREATED);
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(authService.getRoleById(id));
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(authService.getAllRoles());
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        authService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    // 🔗 ধাপ ২: রোলকে মডিউল অ্যাসাইন করা
    @PostMapping("/roles/{roleId}/assign-modules")
    public ResponseEntity<Map<String, String>> assignModulesToRole(
            @PathVariable Long roleId,
            @RequestBody Set<Long> moduleIds) {
        authService.assignModulesToRole(roleId, moduleIds);
        return ResponseEntity.ok(Map.of("message", "Modules assigned to role successfully"));
    }

    // ==========================================
    // 📦 3. MODULE API'S (CRUD & ASSIGNMENT)
    // ==========================================

    @PostMapping("/modules")
    public ResponseEntity<Module> createModule(@RequestBody Module module) {
        return new ResponseEntity<>(authService.createModule(module), HttpStatus.CREATED);
    }

    @GetMapping("/modules/{id}")
    public ResponseEntity<Module> getModuleById(@PathVariable Long id) {
        return ResponseEntity.ok(authService.getModuleById(id));
    }

    @GetMapping("/modules")
    public ResponseEntity<List<Module>> getAllModules() {
        return ResponseEntity.ok(authService.getAllModules());
    }

    @DeleteMapping("/modules/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        authService.deleteModule(id);
        return ResponseEntity.noContent().build();
    }

    // 🔗 ধাপ ৩: মডিউলকে পেজ অ্যাসাইন করা
    @PostMapping("/modules/{moduleId}/assign-pages")
    public ResponseEntity<Map<String, String>> assignPagesToModule(
            @PathVariable Long moduleId,
            @RequestBody Set<Long> pageIds) {
        authService.assignPagesToModule(moduleId, pageIds);
        return ResponseEntity.ok(Map.of("message", "Pages assigned to module successfully"));
    }

    // ==========================================
    // 📄 4. PAGE API'S (CRUD & ASSIGNMENT)
    // ==========================================

    @PostMapping("/pages")
    public ResponseEntity<Page> createPage(@RequestBody Page page) {
        return new ResponseEntity<>(authService.createPage(page), HttpStatus.CREATED);
    }

    @GetMapping("/pages/{id}")
    public ResponseEntity<Page> getPageById(@PathVariable Long id) {
        return ResponseEntity.ok(authService.getPageById(id));
    }

    @GetMapping("/pages")
    public ResponseEntity<List<Page>> getAllPages() {
        return ResponseEntity.ok(authService.getAllPages());
    }

    @DeleteMapping("/pages/{id}")
    public ResponseEntity<Void> deletePage(@PathVariable Long id) {
        authService.deletePage(id);
        return ResponseEntity.noContent().build();
    }

    // 🔗 ধাপ ৪: পেজকে সুনির্দিষ্ট পারমিশন অ্যাসাইন করা
    @PostMapping("/pages/{pageId}/assign-permissions")
    public ResponseEntity<Map<String, String>> assignPermissionsToPage(
            @PathVariable Long pageId,
            @RequestBody Set<Long> permissionIds) {
        authService.assignPermissionsToPage(pageId, permissionIds);
        return ResponseEntity.ok(Map.of("message", "Permissions assigned to page successfully"));
    }

    // ==========================================
    // 🔑 5. PERMISSION API'S (CRUD ONLY)
    // ==========================================

    @PostMapping("/permissions")
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        return new ResponseEntity<>(authService.createPermission(permission), HttpStatus.CREATED);
    }

    @GetMapping("/permissions/{id}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id) {
        return ResponseEntity.ok(authService.getPermissionById(id));
    }

    @GetMapping("/permissions")
    public ResponseEntity<List<Permission>> getAllPermissions() {
        return ResponseEntity.ok(authService.getAllPermissions());
    }

    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        authService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/full-tree/{id}")
    public ResponseEntity<UserResponseDTO> getUserWithFullTree(@PathVariable Long id) {
        return ResponseEntity.ok(authService.getUserWithFullTree(id));
    }
}
