package com.example.auth.impl;

import com.example.auth.dto.RoleParam;
import com.example.auth.dto.RoleDto;
import com.example.auth.mapper.EntityMapper;
import com.example.auth.repository.ModuleRepository;
import com.example.auth.repository.RoleRepository;
import com.example.auth.service.RoleService;
import com.example.common.model.Module;
import com.example.common.model.Role;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModuleRepository moduleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModuleRepository moduleRepository) {
        this.roleRepository = roleRepository;
        this.moduleRepository = moduleRepository;
    }

    @Override
    @Transactional
    public RoleDto createRole(RoleParam param) {
        if (roleRepository.findByName(param.getName()).isPresent()) {
            throw new IllegalArgumentException("Role already exists with name: " + param.getName());
        }

        Role role = new Role();
        role.setName(param.getName());

        Set<Module> modules = new HashSet<>();
        if (param.getModuleIds() != null && !param.getModuleIds().isEmpty()) {
            modules = param.getModuleIds().stream()
                    .map(moduleId -> moduleRepository.findById(moduleId)
                            .orElseThrow(() -> new EntityNotFoundException("Module not found with id: " + moduleId)))
                    .collect(Collectors.toSet());
        }
        role.setModules(modules);

        Role savedRole = roleRepository.save(role);
        return EntityMapper.toRoleDto(savedRole);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id));
        return EntityMapper.toRoleDto(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(EntityMapper::toRoleDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public RoleDto assignModulesToRole(Long roleId, Set<Long> moduleIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));
        List<Module> modules = moduleRepository.findAllById(moduleIds);
        role.setModules(new HashSet<>(modules));
        Role savedRole = roleRepository.save(role);
        return EntityMapper.toRoleDto(savedRole);
    }
}
