package com.example.demo.service

import com.example.demo.model.ApplicationUser
import com.example.demo.model.Permission
import com.example.demo.model.Role
import com.example.demo.repository.PermissionRepository
import com.example.demo.repository.RoleRepository
import com.example.demo.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//import org.springframework.security.core.userdetails.User
//import org.springframework.security.core.userdetails.UserDetails
//import org.springframework.security.core.userdetails.UserDetailsService
//import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
private val userRepository: UserRepository,
private val roleRepository: RoleRepository,
private val permissionRepository: PermissionRepository,
): UserService, UserDetailsService {

    override fun saveUser(user: ApplicationUser): ApplicationUser =
        userRepository.save(user)

    override fun saveRole(role: Role): Role =
        roleRepository.save(role)

    override fun savePermission(permission: Permission): Permission =
        permissionRepository.save(permission)

    override fun getAllApplicationUsers(): List<ApplicationUser> =
        userRepository.findAll()

    override fun getApplicationUserByUsername(username: String): ApplicationUser =
        userRepository.findByUsername(username)

    override fun loadUserByUsername(username: String?): UserDetails {
        val user = username?.let { userRepository.findByUsername(it) }?: throw UsernameNotFoundException("user with this username not found")

        val authorities: MutableSet<GrantedAuthority> = mutableSetOf()

        user.roles.forEach {
            authorities.add(SimpleGrantedAuthority("ROLE_"+it.name))

            for(permission in it.permissions){
                authorities.add(SimpleGrantedAuthority(permission.description))
            }
        }

        //println(authorities)

        user.grantedAuthorities = authorities

        return user
    }
}