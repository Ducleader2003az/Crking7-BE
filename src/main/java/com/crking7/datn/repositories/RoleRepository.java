package com.crking7.datn.repositories;

import com.crking7.datn.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findById(long roleId);
}
