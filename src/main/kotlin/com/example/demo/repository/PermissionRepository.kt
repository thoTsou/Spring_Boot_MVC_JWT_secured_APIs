package com.example.demo.repository

import com.example.demo.model.Permission
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PermissionRepository: JpaRepository<Permission, Long> {
    fun findByDescription(description: String): Permission
}