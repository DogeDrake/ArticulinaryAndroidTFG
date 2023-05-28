package com.example.articulinarytfg


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RecetasPopulateResponse(
    val `data`: List<Data>,
    val meta: Meta
):Serializable {
    data class Data(
        val attributes: Attributes,
        val id: Int
    ):Serializable {
        data class Attributes(
            val createdAt: String,
            @SerializedName("Gente")
            val gente: Int,
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
            val likes: Likes,
            @SerializedName("LikesID")
            val likesID: String,
            @SerializedName("PasosTexto")
            val pasosTexto: String,
            val publishedAt: String,
            @SerializedName("Tiempo")
            val tiempo: Int,
            @SerializedName("Titulo")
            val titulo: String,
            val updatedAt: String,
            val user: User
        ):Serializable {
            data class Likes(
                val `data`: List<Any>
            )

            data class User(
                val `data`: Data
            ):Serializable {
                data class Data(
                    val attributes: Attributes,
                    val id: Int
                ):Serializable {
                    data class Attributes(
                        val blocked: Boolean,
                        val confirmed: Boolean,
                        val createdAt: String,
                        val email: String,
                        val password: String,
                        val provider: String,
                        @SerializedName("RealName")
                        val realName: String,
                        val updatedAt: String,
                        @SerializedName("UserImg")
                        val userImg: String,
                        val username: String
                    )
                }
            }
        }
    }

    data class Meta(
        val pagination: Pagination
    ):Serializable {
        data class Pagination(
            val page: Int,
            val pageCount: Int,
            val pageSize: Int,
            val total: Int
        )
    }
}