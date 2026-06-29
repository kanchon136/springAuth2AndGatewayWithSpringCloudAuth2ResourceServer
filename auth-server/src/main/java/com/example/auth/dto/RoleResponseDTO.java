package com.example.auth.dto;


import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponseDTO {
        private Long id;
        private String name;
        private Set<ModuleResponseDTO> modules;
}
