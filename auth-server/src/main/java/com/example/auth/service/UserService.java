package com.example.auth.service;

import com.example.auth.repository.UserRepository;
import com.example.common.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // ১. ইউজার খুঁজে বের করা
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // ২. ডুপ্লিকেট এড়ানোর জন্য Set তৈরি করা
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        // ৩. নেস্টেড হায়ারার্কি লুপ (রোল -> মডিউল -> পেজ -> পারমিশন)
        if (user.getRoles() != null) {
            user.getRoles().forEach(role -> {
                // ক) রোলের নাম যোগ করা (e.g., ROLE_ADMIN)
                // আপনার এনটিটিতে যদি অলরেডি "ROLE_" প্রিফিক্স না থাকে, তবে এখানে কনক্যাট করে দিন
                String roleName = role.getName().startsWith("ROLE_") ? role.getName() : "ROLE_" + role.getName();
                authorities.add(new SimpleGrantedAuthority(roleName));

                // খ) ঐ রোলের আন্ডারে থাকা সব মডিউলে ঢোকা
                if (role.getModules() != null) {
                    role.getModules().forEach(module -> {

                        // গ) ঐ মডিউলের আন্ডারে থাকা সব পেজে ঢোকা
                        if (module.getPages() != null) {
                            module.getPages().forEach(page -> {

                                // ঘ) ঐ পেজের আন্ডারে থাকা সব সুনির্দিষ্ট পারমিশন বের করা
                                if (page.getPermissions() != null) {
                                    page.getPermissions().forEach(permission -> {
                                        // শেষ মাথার ফাইন-গ্রেইন্ড পারমিশন যোগ করা (e.g., HR:EMPLOYEE_PAGE:CREATE)
                                        authorities.add(new SimpleGrantedAuthority(permission.getName()));
                                    });
                                }
                            });
                        }
                    });
                }
            });
        }

        // ৪. স্প্রিং সিকিউরিটির ইউজার অবজেক্ট বিল্ড করে রিটার্ন করা
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(!user.isEnabled())
                .authorities(authorities)
                .build();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
