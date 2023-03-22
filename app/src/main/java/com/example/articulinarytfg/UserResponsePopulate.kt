package com.example.articulinarytfg


import com.google.gson.annotations.SerializedName

class UserResponsePopulate : ArrayList<UserResponsePopulate.UserResponsePopulateItem>() {
    data class UserResponsePopulateItem(
        val blocked: Boolean,
        val confirmed: Boolean,
        val createdAt: String,
        val email: String,
        val id: Int,
        val password: String,
        val provider: String,
        @SerializedName("RealName")
        val realName: String,
        val role: Role,
        val updatedAt: String,
        @SerializedName("UserImg")
        val userImg: Any,
        val username: String
    ) : java.io.Serializable

    data class Role(
        val createdAt: String,
        val description: String,
        val id: Int,
        val name: String,
        val type: String,
        val updatedAt: String
    ) : java.io.Serializable
}
