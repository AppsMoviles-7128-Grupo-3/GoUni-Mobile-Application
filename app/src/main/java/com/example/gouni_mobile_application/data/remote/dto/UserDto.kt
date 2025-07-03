package com.example.gouni_mobile_application.data.remote.dto

data class UserDto(
    val id: Long? = null,
    val name: String,
    val email: String,
    val university: String,
    val userCode: String
) 