package com.example.demo.config

import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.crypto.SecretKey

@Configuration
class JwtSecretKey() {

    @Bean
    fun secretKey(): SecretKey =
        Keys.hmacShaKeyFor("secretsecretsecretsecretsecretsecretsecretsecretsecretsecret".toByteArray())

}