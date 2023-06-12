package com.example.articulinarytfg


import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserResponsePopulate : ArrayList<UserResponsePopulate.UserResponsePopulateItem>() {
    data class UserResponsePopulateItem(
        val blocked: Boolean,
        val confirmed: Boolean,
        val createdAt: String,
        val email: String,
        val id: Int,
        val provider: String,
        @SerializedName("RealName")
        val realName: String,
        val recetas: List<Receta>,
        val role: Role,
        val updatedAt: String,
        @SerializedName("UserImg")
        val userImg: String,
        val username: String
    ) : Serializable {
        data class Receta(
            val createdAt: String,
            @SerializedName("Gente")
            val gente: Int,
            val id: Int,
            @SerializedName("Imagen")
            val imagen: String,
            @SerializedName("IngredientesTexto")
            val ingredientesTexto: String,
            @SerializedName("IsBajoEnAzucar")
            val isBajoEnAzucar: Boolean,
            @SerializedName("IsSinGluten")
            val isSinGluten: Boolean,
            @SerializedName("IsSinLactosa")
            val isSinLactosa: Boolean,
            @SerializedName("IsVegano")
            val isVegano: Boolean,
            @SerializedName("IsVegetariano")
            val isVegetariano: Boolean,
            @SerializedName("LikesID")
            val likesID: String,
            @SerializedName("PasosTexto")
            val pasosTexto: String,
            val publishedAt: String,
            @SerializedName("Tiempo")
            val tiempo: Int,
            @SerializedName("Titulo")
            val titulo: String,
            val updatedAt: String
        ) : Serializable

        data class Role(
            val createdAt: String,
            val description: String,
            val id: Int,
            val name: String,
            val type: String,
            val updatedAt: String
        ) : Serializable
    }
}