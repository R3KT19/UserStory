package com.batararaja.userstory

data class RegisterInfo(
    val name: String,
    val email: String,
    val password: String)

data class LoginInfo(
    val email: String,
    val password: String)

