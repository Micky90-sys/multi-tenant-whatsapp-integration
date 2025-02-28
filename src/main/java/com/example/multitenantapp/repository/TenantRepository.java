package com.example.multitenantapp.repository;

import com.example.multitenantapp.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {}
