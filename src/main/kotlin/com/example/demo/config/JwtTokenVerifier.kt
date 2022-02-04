package com.example.demo.config

import org.springframework.web.filter.OncePerRequestFilter
import javax.crypto.SecretKey
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import com.google.common.base.Strings
import com.google.common.net.HttpHeaders
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

class JwtTokenVerifier(
    private val secretKey: SecretKey
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        if(Strings.isNullOrEmpty(authHeader) || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response)
            return
        }

        val token = authHeader.replace("Bearer " , "")

        try {

            val claimsJws = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)

            val body = claimsJws.body
            val username = body.subject
            val authorities = body["authorities"] as List<Map<String,String>>

            val simpleGrantedAuthorities = authorities.map {
                SimpleGrantedAuthority(it["authority"])
            }

            //println(simpleGrantedAuthorities)

            val authentication: Authentication =
                UsernamePasswordAuthenticationToken(username,null,simpleGrantedAuthorities)

            SecurityContextHolder.getContext().authentication = authentication

        }catch (e: JwtException){
            throw e
        }

        filterChain.doFilter(request,response)
    }
}