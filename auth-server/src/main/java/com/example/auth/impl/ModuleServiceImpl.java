package com.example.auth.impl;

import com.example.auth.dto.ModuleDto;
import com.example.auth.dto.ModuleParam;
import com.example.auth.mapper.EntityMapper;
import com.example.auth.repository.ModuleRepository;
import com.example.auth.repository.PageRepository;
import com.example.auth.service.ModuleService;
import com.example.common.model.Module;
import com.example.common.model.Page;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final PageRepository pageRepository;

    @Autowired
    public ModuleServiceImpl(ModuleRepository moduleRepository, PageRepository pageRepository) {
        this.moduleRepository = moduleRepository;
        this.pageRepository = pageRepository;
    }

    @Override
    @Transactional
    public ModuleDto createModule(ModuleParam param) {
        Module module = new Module();
        module.setName(param.getName());
        module.setIcon(param.getIcon());

        Set<Page> pages = new HashSet<>();
        if (param.getPageIds() != null && !param.getPageIds().isEmpty()) {
            pages = param.getPageIds().stream()
                    .map(pageId -> pageRepository.findById(pageId)
                            .orElseThrow(() -> new EntityNotFoundException("Page not found with id: " + pageId)))
                    .collect(Collectors.toSet());
        }
        module.setPages(pages);

        Module savedModule = moduleRepository.save(module);
        return EntityMapper.toModuleDto(savedModule);
    }

    @Override
    @Transactional(readOnly = true)
    public ModuleDto getModuleById(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Module not found with id: " + id));
        return EntityMapper.toModuleDto(module);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuleDto> getAllModules() {
        return moduleRepository.findAll().stream()
                .map(EntityMapper::toModuleDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteModule(Long id) {
        if (!moduleRepository.existsById(id)) {
            throw new EntityNotFoundException("Module not found with id: " + id);
        }
        moduleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ModuleDto assignPagesToModule(Long moduleId, Set<Long> pageIds) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new EntityNotFoundException("Module not found with id: " + moduleId));
        List<Page> pages = pageRepository.findAllById(pageIds);
        module.setPages(new HashSet<>(pages));
        Module savedModule = moduleRepository.save(module);
        return EntityMapper.toModuleDto(savedModule);
    }
}
