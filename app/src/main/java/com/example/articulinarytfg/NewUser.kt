package com.example.articulinarytfg

import com.google.gson.annotations.SerializedName

data class User(
    val username: String,
    val email: String,
    val password: String,
    val role: Int
)