package com.example.common.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "auth_permissions", indexes = {
        @Index(name = "idx_auth_permission_name", columnList = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Permission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name; // গ্লোবাল ইউনিক নাম, যেমন: "HR:EMPLOYEE_PAGE:CREATE"

    @Column(nullable = false, length = 50)
    private String action; // অ্যাকশনের ধরণ, যেমন: "CREATE", "READ", "UPDATE", "DELETE"

    @Column(length = 255)
    private String description;
}
