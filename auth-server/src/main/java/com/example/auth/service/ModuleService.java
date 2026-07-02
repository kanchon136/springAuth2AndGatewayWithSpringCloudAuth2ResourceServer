package com.example.auth.service;

import com.example.auth.dto.ModuleParam;
import com.example.auth.dto.ModuleDto;

import java.util.List;
import java.util.Set;

public interface ModuleService {
    ModuleDto createModule(ModuleParam param);

    ModuleDto getModuleById(Long id);

    List<ModuleDto> getAllModules();

    void deleteModule(Long id);

    ModuleDto assignPagesToModule(Long moduleId, Set<Long> pageIds);
}
