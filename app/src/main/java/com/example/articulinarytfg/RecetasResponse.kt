package com.example.articulinarytfg


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RecetasResponse(
    @SerializedName("data")
    val data: Data,
    val meta: Meta
) {
    data class Data(
        val attributes: Attributes,
        val id: Int
    )

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
        @SerializedName("LikesID")
        val LikesID: String,
        @SerializedName("PasosTexto")
        val pasosTexto: String,
        val publishedAt: String,
        @SerializedName("Tiempo")
        val tiempo: Int,
        @SerializedName("Titulo")
        val titulo: String,
        val updatedAt: String
    )

    data class Meta(
        val pagination: Pagination
    )

    data class Pagination(
        val page: Int,
        val pageCount: Int,
        val pageSize: Int,
        val total: Int
    )
}
