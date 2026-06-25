package com.example.common.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "auth_users", indexes = {
        @Index(name = "idx_auth_user_username", columnList = "username"),
        @Index(name = "idx_auth_user_email", columnList = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Builder.Default
    private boolean enabled = true;

    // ইউজারকে রোল অ্যাসাইন করার ম্যাপিং টেবিল
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "auth_user_roles_mapping",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
