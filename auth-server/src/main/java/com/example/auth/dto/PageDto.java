package com.example.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDto {
    private Long id;
    private String name;
    private String urlPath;
    private Set<PermissionDto> permissions;
}
