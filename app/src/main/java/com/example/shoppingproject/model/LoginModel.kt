package com.example.shoppingproject.model

data class LoginModel(
    val isSuccess: Boolean = false,
    val email: String = "",
    val password: String = ""
)