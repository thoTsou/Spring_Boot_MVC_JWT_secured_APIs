package com.example.demo.config

import com.example.demo.config.filters.JwtAuthenticationFilter
import com.example.demo.service.UserServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import javax.crypto.SecretKey

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfiguration(
    private val passwordEncoder: PasswordEncoder,
    private val userService: UserServiceImpl,
    private val secretKey: SecretKey,
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(JwtAuthenticationFilter(authenticationManager(), secretKey))
            .addFilterAfter(JwtTokenVerifier(secretKey), JwtAuthenticationFilter::class.java)
            .authorizeRequests()
            .antMatchers("/my-api/demo").permitAll()
            .antMatchers("/my-api/token/refresh").permitAll()
//            .antMatchers("/api/**").hasRole("role name goes here")
            .anyRequest()
            .authenticated();
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(daoAuthenticationProvider())
    }

    @Bean
    fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setPasswordEncoder(passwordEncoder)
        provider.setUserDetailsService(userService)
        return provider
    }

}