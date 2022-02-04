package com.example.demo.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.net.HttpHeaders
import io.jsonwebtoken.Jwts
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*
import javax.crypto.SecretKey
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.ArrayList

@Service
class TokenRefreshService(
    private val userService: UserService,
    private val secretKey: SecretKey
) {

    fun refreshApplicationUsersToken(request: HttpServletRequest, response: HttpServletResponse){
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            try{
                val refresh_token = authHeader.substring("Bearer ".length)

                val user = userService.getApplicationUserByUsername(
                    Jwts.parserBuilder().setSigningKey(secretKey).build()
                        .parseClaimsJws(refresh_token)
                        .body.subject
                )

                val authorities = Jwts.parserBuilder().setSigningKey(secretKey).build()
                    .parseClaimsJws(refresh_token)
                    .body["authorities"] as ArrayList<*>

                val access_token = Jwts.builder()
                    .setSubject(user.username)
                    .claim("authorities", authorities)
                    .setIssuedAt(Date())
                    .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(10)))
                    .signWith(secretKey)
                    .compact()

                val new_refresh_token = Jwts.builder()
                    .setSubject(user.username)
                    .claim("authorities", authorities)
                    .setIssuedAt(Date())
                    .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(30)))
                    .signWith(secretKey)
                    .compact()

                val tokens = mutableMapOf("access_token" to access_token , "refresh_token" to new_refresh_token)
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                ObjectMapper().writeValue(response.outputStream, tokens)
            }catch (e: Exception){
                response.setHeader("error",e.message)
                response.status = 403
                //response.sendError(FORBIDDEN.value())
                val error = mutableMapOf("error" to e.message )
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                ObjectMapper().writeValue(response.outputStream,error)
            }
        }else{
            throw RuntimeException("refresh token is missing")
        }
    }

}