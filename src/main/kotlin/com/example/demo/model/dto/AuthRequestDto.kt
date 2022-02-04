package com.example.demo.model.dto

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

@NoArgsConstructor
@AllArgsConstructor
data class AuthRequestDto(
    @JsonProperty("username")
    val username: String,
    @JsonProperty("password")
    val password: String
)
