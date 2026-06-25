package com.example.auth.repository;

import com.example.common.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module,Long> {
    Optional<Module> findByName(String name);
}
