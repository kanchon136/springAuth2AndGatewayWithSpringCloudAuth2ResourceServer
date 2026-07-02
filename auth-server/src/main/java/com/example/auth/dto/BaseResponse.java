package com.example.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {

    private String message;
    private String errorData;
    private ResponseEnum responseEnum;

    // User DTO fields
    private UserDto userDto;
    private List<UserDto> userDtos;
    private PaginatedResponse<UserDto> userDtoPaginatedResponse;

    // Role DTO fields
    private RoleDto roleDto;
    private List<RoleDto> roleDtos;
    private PaginatedResponse<RoleDto> roleDtoPaginatedResponse;

    // Module DTO fields
    private ModuleDto moduleDto;
    private List<ModuleDto> moduleDtos;
    private PaginatedResponse<ModuleDto> moduleDtoPaginatedResponse;

    // Page DTO fields
    private PageDto pageDto;
    private List<PageDto> pageDtos;
    private PaginatedResponse<PageDto> pageDtoPaginatedResponse;

    // Permission DTO fields
    private PermissionDto permissionDto;
    private List<PermissionDto> permissionDtos;
    private PaginatedResponse<PermissionDto> permissionDtoPaginatedResponse;
}
