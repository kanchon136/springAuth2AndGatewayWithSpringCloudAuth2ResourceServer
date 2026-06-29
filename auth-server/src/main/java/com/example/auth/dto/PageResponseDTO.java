package com.example.auth.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponseDTO {

    private Long id;
    private String name;
    private String urlPath;
    private Set<PermissionResponseDTO> permissions;
}
