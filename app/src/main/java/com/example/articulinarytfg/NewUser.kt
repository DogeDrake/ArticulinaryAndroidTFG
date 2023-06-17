package com.example.articulinarytfg

import com.google.gson.annotations.SerializedName
import java.io.Serializable
data class User(
    val username: String,
    val RealName: String,
    val email: String,
    val UserImg: String,
    val role: Int
):Serializable