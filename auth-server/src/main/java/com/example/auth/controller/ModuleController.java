package com.example.auth.controller;

import com.example.auth.dto.ModuleDto;
import com.example.auth.dto.ModuleParam;
import com.example.auth.dto.BaseResponse;
import com.example.auth.dto.ResponseEnum;
import com.example.auth.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth-management/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @PostMapping
    public ResponseEntity<BaseResponse> createModule(@RequestBody ModuleParam param) {
        ModuleDto moduleDto = moduleService.createModule(param);
        return new ResponseEntity<>(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Module created successfully")
                .moduleDto(moduleDto)
                .build(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<BaseResponse> getAllModules() {
        List<ModuleDto> modules = moduleService.getAllModules();
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Modules retrieved successfully")
                .moduleDtos(modules)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getModuleById(@PathVariable Long id) {
        ModuleDto moduleDto = moduleService.getModuleById(id);
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Module retrieved successfully")
                .moduleDto(moduleDto)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteModule(@PathVariable Long id) {
        moduleService.deleteModule(id);
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Module deleted successfully")
                .build());
    }

    @PostMapping("/{moduleId}/assign-pages")
    public ResponseEntity<BaseResponse> assignPagesToModule(
            @PathVariable Long moduleId,
            @RequestBody Set<Long> pageIds) {
        ModuleDto moduleDto = moduleService.assignPagesToModule(moduleId, pageIds);
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Pages assigned to module successfully")
                .moduleDto(moduleDto)
                .build());
    }
}
