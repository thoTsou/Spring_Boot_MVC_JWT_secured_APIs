package com.example.demo

import com.example.demo.model.ApplicationUser
import com.example.demo.model.Permission
import com.example.demo.model.Role
import com.example.demo.repository.PermissionRepository
import com.example.demo.repository.RoleRepository
import com.example.demo.service.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication
class DemoApplication{

	// this app is made just for fun and testing
	@Bean
	fun run(
		userService: UserService,
		permissionRepository: PermissionRepository,
		roleRepository: RoleRepository
	): CommandLineRunner {
		return CommandLineRunner { args ->

			userService.savePermission(Permission(description = "admin:read"))
			userService.savePermission(Permission(description = "admin:write"))
			userService.savePermission(Permission(description = "nonAdmin:read"))
			userService.savePermission(Permission(description = "nonAdmin:write"))

			userService.saveRole(Role(name = "ADMIN", permissions = mutableListOf(
				permissionRepository.findByDescription("admin:read"),
				permissionRepository.findByDescription("admin:write"),
			)))

			userService.saveRole(Role(name = "NONADMIN", permissions = mutableListOf(
				permissionRepository.findByDescription("nonAdmin:read"),
				permissionRepository.findByDescription("nonAdmin:write"),
			)))

			userService.saveUser(ApplicationUser(
				//name = "thodoris tsoufis",
				username = "thotsou",
				password = passwordEncoder().encode("12345"),
				roles = mutableListOf(
					roleRepository.findByName("ADMIN")
				),
				isAccountNonExpired = true,
				isAccountNonLocked = true,
				isCredentialsNonExpired = true,
				isEnabled = true,
				grantedAuthorities = mutableSetOf<GrantedAuthority>()
			))

			userService.saveUser(ApplicationUser(
				//name = "tasos tsantoulis",
				username = "taSOS",
				password = passwordEncoder().encode("6789"),
				roles = mutableListOf(
					roleRepository.findByName("NONADMIN")
				),
				isAccountNonExpired = true,
				isAccountNonLocked = true,
				isCredentialsNonExpired = true,
				isEnabled = true,
				grantedAuthorities = mutableSetOf<GrantedAuthority>()
			))
		}
	}

	@Bean
	fun passwordEncoder(): PasswordEncoder =
		BCryptPasswordEncoder(10)

}

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
