package com.example.shoppingproject.model

data class RegisterModel(
    val isSuccess: Boolean = false,
    val email: String = "",
    val password: String = ""
)