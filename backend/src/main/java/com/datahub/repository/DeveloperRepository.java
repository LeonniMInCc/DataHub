package com.datahub.repository;

import com.datahub.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    Optional<Developer> findByUsername(String username);
    Optional<Developer> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    long countByRole(Developer.Role role);
}
