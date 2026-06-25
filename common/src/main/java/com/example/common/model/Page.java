package com.example.common.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "auth_pages", indexes = {
        @Index(name = "idx_auth_page_name", columnList = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Page extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name; // যেমন: "EMPLOYEE_LIST_PAGE", "ADD_PRODUCT_PAGE"

    @Column(nullable = false, length = 255)
    private String urlPath; // ফ্রন্টএন্ড রাউটিং-এর পাথ (যেমন: "/hr/employees")

    // পেজের অধীনে সুনির্দিষ্ট পারমিশন অ্যাসাইন করার ম্যাপিং টেবিল
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "auth_page_permissions_mapping",
            joinColumns = @JoinColumn(name = "page_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();
}
