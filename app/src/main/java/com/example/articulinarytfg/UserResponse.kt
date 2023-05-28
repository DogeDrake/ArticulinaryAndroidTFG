package com.example.articulinarytfg


import com.google.gson.annotations.SerializedName

class UserResponse : ArrayList<UserResponse.UserResponseItem>(){
    data class UserResponseItem(
        val blocked: Boolean,
        val confirmed: Boolean,
        val createdAt: String,
        val email: String,
        val id: Int,
        val password: String,
        val provider: String,
        @SerializedName("RealName")
        val realName: String,
        val updatedAt: String,
        @SerializedName("UserImg")
        val userImg: String,
        val username: String
    ):java.io.Serializable
}