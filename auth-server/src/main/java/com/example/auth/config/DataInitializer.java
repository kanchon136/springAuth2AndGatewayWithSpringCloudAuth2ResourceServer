package com.example.auth.config;

import com.example.auth.repository.*;
import com.example.common.enums.RecordStatus;
import com.example.common.model.Permission;
import com.example.common.model.Role;
import com.example.common.model.User;
import com.example.common.model.Module;
import com.example.common.model.Page;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModuleRepository moduleRepository;
    private final PageRepository pageRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository,
                           ModuleRepository moduleRepository, PageRepository pageRepository,
                           PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.moduleRepository = moduleRepository;
        this.pageRepository = pageRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Initializing 5-Tier Hierarchical Security Data for SuperAdmin...");

        // ১. শেষ মাথার সুনির্দিষ্ট পারমিশন তৈরি (Permissions)
        Permission userCreate = createPermissionIfNotFound("HR:USER_PAGE:CREATE", "CREATE");
        Permission userRead   = createPermissionIfNotFound("HR:USER_PAGE:READ", "READ");
        Permission userUpdate = createPermissionIfNotFound("HR:USER_PAGE:UPDATE", "UPDATE");
        Permission userDelete = createPermissionIfNotFound("HR:USER_PAGE:DELETE", "DELETE");

        Permission roleCreate = createPermissionIfNotFound("SETTING:ROLE_PAGE:CREATE", "CREATE");
        Permission roleRead   = createPermissionIfNotFound("SETTING:ROLE_PAGE:READ", "READ");

        // ২. পেজ তৈরি এবং পারমিশন অ্যাসাইন (Pages)
        com.example.common.model.Page userPage = createPageIfNotFound("USER_MANAGEMENT_PAGE", "/hr/users",
                new HashSet<>(Set.of(userCreate, userRead, userUpdate, userDelete)));

        com.example.common.model.Page rolePage = createPageIfNotFound("ROLE_MANAGEMENT_PAGE", "/settings/roles",
                new HashSet<>(Set.of(roleCreate, roleRead)));

        // ৩. মডিউল তৈরি এবং পেজ অ্যাসাইন (Modules)
        com.example.common.model.Module hrModule = createModuleIfNotFound("HR_MODULE", "fa-users",
                new HashSet<>(Set.of(userPage)));

        com.example.common.model.Module settingModule = createModuleIfNotFound("SETTING_MODULE", "fa-cogs",
                new HashSet<>(Set.of(rolePage)));

        // ৪. সুপারএডমিন রোল তৈরি এবং মডিউল অ্যাসাইন (Role)
        Role superAdminRole = createRoleIfNotFound("ROLE_SUPERADMIN", "Full Access Role",
                new HashSet<>(Set.of(hrModule, settingModule)));

        // ৫. সুপারএডমিন ইউজার তৈরি (User)
        if (!userRepository.existsByUsername("superadmin")) {
            User superAdmin = User.builder()
                    .username("superadmin")
                    .email("superadmin@example.com")
                    .password(passwordEncoder.encode("superadmin123")) // আপনার স্ট্রং পাসওয়ার্ড
                    .roles(new HashSet<>(Set.of(superAdminRole)))
                    .enabled(true)
                    .createdDateTime(LocalDateTime.now())
                    .recordStatus(RecordStatus.ACTIVE)
                    .build();
            userRepository.save(superAdmin);
            log.info("SuperAdmin user created successfully.");
        }

        log.info("Security data initialization completed successfully.");
    }

    private Permission createPermissionIfNotFound(String name, String action) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> permissionRepository.save(Permission.builder()
                        .name(name)
                        .action(action)
                        .createdDateTime(LocalDateTime.now())
                        .recordStatus(RecordStatus.ACTIVE)
                        .build()));
    }

    private Page createPageIfNotFound(String name, String urlPath, Set<Permission> permissions) {
        return pageRepository.findByName(name) // আপনার PageRepository তে findByName থাকতে হবে
                .map(page -> {
                    page.setPermissions(permissions);
                    return pageRepository.save(page);
                })
                .orElseGet(() -> pageRepository.save(Page.builder()
                        .name(name)
                        .urlPath(urlPath)
                        .permissions(permissions)
                        .createdDateTime(LocalDateTime.now())
                        .recordStatus(RecordStatus.ACTIVE)
                        .build()));
    }

    private Module createModuleIfNotFound(String name, String icon, Set<Page> pages) {
        return moduleRepository.findByName(name) // আপনার ModuleRepository তে findByName থাকতে হবে
                .map(module -> {
                    module.setPages(pages);
                    return moduleRepository.save(module);
                })
                .orElseGet(() -> moduleRepository.save(Module.builder()
                        .name(name)
                        .icon(icon)
                        .pages(pages)
                        .createdDateTime(LocalDateTime.now())
                        .recordStatus(RecordStatus.ACTIVE)
                        .build()));
    }

    private Role createRoleIfNotFound(String name, String description, Set<com.example.common.model.Module> modules) {
        return roleRepository.findByName(name)
                .map(role -> {
                    role.setModules(modules);
                    return roleRepository.save(role);
                })
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name(name)
                        .description(description)
                        .modules(modules)
                        .createdDateTime(LocalDateTime.now())
                        .recordStatus(RecordStatus.ACTIVE)
                        .build()));
    }
}
