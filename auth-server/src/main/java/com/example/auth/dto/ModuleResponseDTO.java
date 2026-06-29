package com.example.auth.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ModuleResponseDTO {

    private Long id;
    private String name;
    private String icon;
    private Set<PageResponseDTO> pages;
}
