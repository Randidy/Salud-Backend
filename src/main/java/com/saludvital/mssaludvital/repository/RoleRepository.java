package com.saludvital.mssaludvital.repository;

import com.saludvital.mssaludvital.entity.Role;
import com.saludvital.mssaludvital.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}