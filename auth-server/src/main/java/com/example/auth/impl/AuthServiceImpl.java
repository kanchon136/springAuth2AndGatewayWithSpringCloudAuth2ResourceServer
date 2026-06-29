package com.example.auth.impl;

import com.example.auth.dto.*;
import com.example.auth.repository.*;
import com.example.auth.service.AuthService;
import com.example.common.model.Page;
import com.example.common.model.Module;
import com.example.common.model.Permission;
import com.example.common.model.Role;
import com.example.common.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModuleRepository moduleRepository;
    private final PageRepository pageRepository;
    private final PermissionRepository permissionRepository;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           ModuleRepository moduleRepository, PageRepository pageRepository,
                           PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.moduleRepository = moduleRepository;
        this.pageRepository = pageRepository;
        this.permissionRepository = permissionRepository;
    }

    // ==========================================
    // 👤 USER CRUD & ASSIGNMENT
    // ==========================================
    @Override
    public User createUser(User user) { return userRepository.save(user); }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() { return userRepository.findAll(); }

    @Override
    public void deleteUser(Long id) { userRepository.deleteById(id); }

    @Override
    public void assignRolesToUser(Long userId, Set<Long> roleIds) {
        User user = getUserById(userId);
        List<Role> roles = roleRepository.findAllById(roleIds);
        user.setRoles(new HashSet<>(roles));
        userRepository.save(user); // Mapping টেবিল স্বয়ংক্রিয়ভাবে আপডেট হবে
    }

    // ==========================================
    // 🎖️ ROLE CRUD & ASSIGNMENT
    // ==========================================
    @Override
    public Role createRole(Role role) { return roleRepository.save(role); }

    @Override
    @Transactional(readOnly = true)
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() { return roleRepository.findAll(); }

    @Override
    public void deleteRole(Long id) { roleRepository.deleteById(id); }

    @Override
    public void assignModulesToRole(Long roleId, Set<Long> moduleIds) {
        Role role = getRoleById(roleId);
        List<com.example.common.model.Module> modules = moduleRepository.findAllById(moduleIds);
        role.setModules(new HashSet<>(modules));
        roleRepository.save(role);
    }

    // ==========================================
    // 📦 MODULE CRUD & ASSIGNMENT
    // ==========================================
    @Override
    public Module createModule(Module module) { return moduleRepository.save(module); }

    @Override
    @Transactional(readOnly = true)
    public Module getModuleById(Long id) {
        return moduleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Module not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Module> getAllModules() { return moduleRepository.findAll(); }

    @Override
    public void deleteModule(Long id) { moduleRepository.deleteById(id); }

    @Override
    public void assignPagesToModule(Long moduleId, Set<Long> pageIds) {
        Module module = getModuleById(moduleId);
        List<Page> pages = pageRepository.findAllById(pageIds);
        module.setPages(new HashSet<>(pages));
        moduleRepository.save(module);
    }

    // ==========================================
    // 📄 PAGE CRUD & ASSIGNMENT
    // ==========================================
    @Override
    public Page createPage(Page page) { return pageRepository.save(page); }

    @Override
    @Transactional(readOnly = true)
    public Page getPageById(Long id) {
        return pageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Page not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Page> getAllPages() { return pageRepository.findAll(); }

    @Override
    public void deletePage(Long id) { pageRepository.deleteById(id); }

    @Override
    public void assignPermissionsToPage(Long pageId, Set<Long> permissionIds) {
        Page page = getPageById(pageId);
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        page.setPermissions(new HashSet<>(permissions));
        pageRepository.save(page);
    }

    // ==========================================
    // 🔑 PERMISSION CRUD ONLY
    // ==========================================
    @Override
    public Permission createPermission(Permission permission) { return permissionRepository.save(permission); }

    @Override
    @Transactional(readOnly = true)
    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Permission not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permission> getAllPermissions() { return permissionRepository.findAll(); }

    @Override
    public void deletePermission(Long id) { permissionRepository.deleteById(id); }


    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserWithFullTree(Long id) {
        log.info("🚀 [Step 1] Fetching User from database for ID: {}", id);

        User user = userRepository.findUserWithAllPermissions(id)
                .orElseThrow(() -> {
                    log.error("❌ User NOT found in database for ID: {}", id);
                    return new jakarta.persistence.EntityNotFoundException("User not found");
                });

        log.info("✅ User found: '{}'. Starting 5-Tier DTO conversion...", user.getUsername());
        UserResponseDTO response = convertToUserResponseDTO(user);

        log.info("🎉 [Success] DTO conversion completed for User: {}", user.getUsername());
        return response;
    }

    // ম্যাপিং কনভার্টার ফাংশন (লগ সহ)
    private UserResponseDTO convertToUserResponseDTO(User user) {
        if (user == null) {
            log.warn("⚠️ convertToUserResponseDTO received a NULL user object!");
            return null;
        }

        log.info("👤 [Tier 1] Mapping User: {}", user.getUsername());

        Set<RoleResponseDTO> roleDTOs = user.getRoles().stream().map(role -> {
            log.info("   🎖️ [Tier 2] Processing Role: {}", role.getName());

            Set<ModuleResponseDTO> moduleDTOs = role.getModules().stream().map(module -> {
                log.info("      📦 [Tier 3] Processing Module: {}", module.getName());

                Set<PageResponseDTO> pageDTOs = module.getPages().stream().map(page -> {
                    log.info("         📄 [Tier 4] Processing Page: {}", page.getName());

                    Set<PermissionResponseDTO> permissionDTOs = page.getPermissions().stream().map(perm -> {
                        log.info("            🔑 [Tier 5] Mapping Final Permission: {} -> Action: {}", perm.getName(), perm.getAction());
                        return PermissionResponseDTO.builder()
                                .id(perm.getId())
                                .name(perm.getName())
                                .action(perm.getAction())
                                .build();
                    }).collect(Collectors.toSet());

                    log.info("         ℹ️ Page '{}' mapped with {} permissions.", page.getName(), permissionDTOs.size());
                    return PageResponseDTO.builder()
                            .id(page.getId())
                            .name(page.getName())
                            .urlPath(page.getUrlPath())
                            .permissions(permissionDTOs)
                            .build();
                }).collect(Collectors.toSet());

                log.info("      ℹ️ Module '{}' mapped with {} pages.", module.getName(), pageDTOs.size());
                return ModuleResponseDTO.builder()
                        .id(module.getId())
                        .name(module.getName())
                        .icon(module.getIcon())
                        .pages(pageDTOs)
                        .build();
            }).collect(Collectors.toSet());

            log.info("   ℹ️ Role '{}' mapped with {} modules.", role.getName(), moduleDTOs.size());
            return RoleResponseDTO.builder()
                    .id(role.getId())
                    .name(role.getName())
                    .modules(moduleDTOs)
                    .build();
        }).collect(Collectors.toSet());

        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .roles(roleDTOs)
                .build();
    }





}
