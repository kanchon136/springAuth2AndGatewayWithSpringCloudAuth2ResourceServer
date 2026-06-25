package com.example.common.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "auth_roles", indexes = {@Index(name = "idx_auth_role_name", columnList = "name")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name; // যেমন: "ROLE_ADMIN", "ROLE_HR_MANAGER"

    @Column(length = 255)
    private String description;

    // রোলের অধীনে মডিউল অ্যাসাইন করার ম্যাপিং টেবিল
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "auth_role_modules_mapping",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id")
    )
    private Set<Module> modules = new HashSet<>();
}
