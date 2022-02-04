package com.example.demo.service

import com.example.demo.model.ApplicationUser
import com.example.demo.model.Permission
import com.example.demo.model.Role

interface UserService {

    fun saveUser(user: ApplicationUser): ApplicationUser
    fun saveRole(role: Role): Role
    fun savePermission(permission: Permission): Permission

    fun getAllApplicationUsers(): List<ApplicationUser>
    fun getApplicationUserByUsername(username: String): ApplicationUser
}