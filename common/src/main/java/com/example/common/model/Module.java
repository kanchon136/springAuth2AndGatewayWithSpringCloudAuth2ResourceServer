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
@Table(name = "auth_modules", indexes = {
        @Index(name = "idx_auth_module_name", columnList = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Module extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name; // যেমন: "INVENTORY_MODULE", "HR_MODULE"

    @Column(length = 255)
    private String description;

    @Column(length = 50)
    private String icon; // ফ্রন্টএন্ড সাইডবার ইউআই-এর আইকন নাম

    // মডিউলের অধীনে পেজ অ্যাসাইন করার ম্যাপিং টেবিল
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "auth_module_pages_mapping",
            joinColumns = @JoinColumn(name = "module_id"),
            inverseJoinColumns = @JoinColumn(name = "page_id")
    )
    private Set<Page> pages = new HashSet<>();
}
