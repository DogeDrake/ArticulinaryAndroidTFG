package com.example.articulinarytfg


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RecetasPopulateResponse(
    val `data`: List<Data>,
) : Serializable {
    data class Data(
        val attributes: Attributes,
        val id: Int
    ) : Serializable {
        data class Attributes(
            val createdAt: String,
            @SerializedName("Gente")
            val gente: Int,
            @SerializedName("Imagen")
            val imagen: Any,
            @SerializedName("IngredientesTexto")
            val ingredientesTexto: String,
            @SerializedName("PasosTexto")
            val pasosTexto: String,
            val publishedAt: String,
            @SerializedName("Tiempo")
            val tiempo: Int,
            @SerializedName("Titulo")
            val titulo: String,
            val updatedAt: String,
            val user: User
        ) : Serializable {
            data class User(
                val `data`: Data
            ) : Serializable {
                data class Data(
                    val attributes: Attributes,
                    val id: Int
                ) : Serializable {
                    data class Attributes(
                        val blocked: Boolean,
                        val confirmed: Boolean,
                        val createdAt: String,
                        val email: String,
                        val provider: String,
                        @SerializedName("RealName")
                        val realName: Any,
                        val updatedAt: String,
                        @SerializedName("UserImg")
                        val userImg: Any,
                        val username: String
                    ) : Serializable
                }
            }
        }
    }
}