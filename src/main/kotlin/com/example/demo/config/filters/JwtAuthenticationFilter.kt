package com.example.demo.config.filters


import com.example.demo.model.dto.AuthRequestDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.net.HttpHeaders
import io.jsonwebtoken.Jwts
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

import java.time.LocalDate
import java.util.*
import javax.crypto.SecretKey
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(
    private val myAuthManager: AuthenticationManager,
    private val secretKey: SecretKey
) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest,
                                       response: HttpServletResponse
    ): Authentication =
        try{
            val authRequest = ObjectMapper().readValue(
                request.inputStream,
                AuthRequestDto::class.java
            )

            val authentication = UsernamePasswordAuthenticationToken(
                authRequest.username,
                authRequest.password
            )

            myAuthManager.authenticate(authentication)
        }catch (e: Exception){
            throw e
        }


    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
//        val token = Jwts.builder()
//            .setSubject(authResult.name)
//            .claim("authorities", authResult.authorities)
//            .setIssuedAt(Date())
//            .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(10)))
//            .signWith(secretKey)
//            .compact()
//
//        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer $token")

        val access_token = Jwts.builder()
            .setSubject(authResult.name)
            .claim("authorities", authResult.authorities)
            .setIssuedAt(Date())
            .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(10)))
            .signWith(secretKey)
            .compact()

        val refresh_token = Jwts.builder()
            .setSubject(authResult.name)
            .claim("authorities", authResult.authorities)
            .setIssuedAt(Date())
            .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(30)))
            .signWith(secretKey)
            .compact()

        val tokens = mutableMapOf("access_token" to access_token , "refresh_token" to refresh_token)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        ObjectMapper().writeValue(response.outputStream, tokens)
    }
}