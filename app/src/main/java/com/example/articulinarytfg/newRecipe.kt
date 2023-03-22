package com.example.articulinarytfg


import com.google.gson.annotations.SerializedName

data class Recipe(
    val `data`: Data
) {
    data class Data(
        @SerializedName("Gente")
        val gente: Int,
        @SerializedName("Imagen")
        val imagen: String,
        @SerializedName("IngredientesTexto")
        val ingredientesTexto: String,
        @SerializedName("PasosTexto")
        val pasosTexto: String,
        @SerializedName("Tiempo")
        val tiempo: Int,
        @SerializedName("Titulo")
        val titulo: String,
        val user: Int
    )
}