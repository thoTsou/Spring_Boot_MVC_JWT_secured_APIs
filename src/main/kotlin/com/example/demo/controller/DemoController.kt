package com.example.demo.controller


import com.example.demo.service.TokenRefreshService
import com.example.demo.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.util.JSONPObject
import com.google.common.net.HttpHeaders
import io.jsonwebtoken.Jwts
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*
import javax.crypto.SecretKey
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.ArrayList

@RestController
@RequestMapping("/my-api")
class DemoController(
    private val userService: UserService,
    private val tokenRefreshService: TokenRefreshService
) {

    //    hasRole('ROLE_')
    //    hasAnyRole('ROLE_')
    //    hasAuthority('permission')
    //    hasAnyAuthority('permission')

    @GetMapping("/admins")
    @PreAuthorize("hasAuthority('admin:read')")
    fun getAllStudents(): String =
        "hello admins"

    @GetMapping("/non-admins")
    @PreAuthorize("hasRole('ROLE_NONADMIN')")
    fun registerNewStudent() =
        "hello non admins"

    // public api, check security config file
    @GetMapping("/demo")
    fun getAll() =
        userService.getAllApplicationUsers()

    // public api, check security config file
    @GetMapping("/token/refresh")
    fun refreshTokens(request: HttpServletRequest, response: HttpServletResponse) =
        tokenRefreshService.refreshApplicationUsersToken(request,response)
}