package com.example.auth.controller;

import com.example.auth.dto.RoleDto;
import com.example.auth.dto.RoleParam;
import com.example.auth.dto.BaseResponse;
import com.example.auth.dto.ResponseEnum;
import com.example.auth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<BaseResponse> createRole(@RequestBody RoleParam param) {
        RoleDto roleDto = roleService.createRole(param);
        return new ResponseEntity<>(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Role created successfully")
                .roleDto(roleDto)
                .build(), HttpStatus.CREATED);
    }

    @GetMapping("/findAllRoles")
    public ResponseEntity<BaseResponse> getAllRoles() {
        List<RoleDto> roles = roleService.getAllRoles();
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Roles retrieved successfully")
                .roleDtos(roles)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getRoleById(@PathVariable Long id) {
        RoleDto roleDto = roleService.getRoleById(id);
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Role retrieved successfully")
                .roleDto(roleDto)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Role deleted successfully")
                .build());
    }

    @PostMapping("/{roleId}/assign-modules")
    public ResponseEntity<BaseResponse> assignModulesToRole(
            @PathVariable Long roleId,
            @RequestBody Set<Long> moduleIds) {
        RoleDto roleDto = roleService.assignModulesToRole(roleId, moduleIds);
        return ResponseEntity.ok(BaseResponse.builder()
                .responseEnum(ResponseEnum.SUCCESS)
                .message("Modules assigned to role successfully")
                .roleDto(roleDto)
                .build());
    }
}
