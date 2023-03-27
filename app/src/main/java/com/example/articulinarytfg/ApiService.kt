package com.example.articulinarytfg

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("Recetas?populate=*")
    fun getRecetasPopulateResponse(
    ): Call<RecetasPopulateResponse>

    @GET("Users")
    fun getUser(
    ): Call<UserResponse>
/*
    @POST("users")
    fun createUser(@Body newUser: NewUser): Call<UserResponse.UserResponseItem>
 */
/*
    @POST("/auth/local/register")
    suspend fun createUser(
        @Body userData: RecetasPopulateResponse.Data.Attributes.User
    ): Response<UserResponsePopulate>

 */


    @POST("users")
    fun registerUser(
        @Body user: User
    ): Call<UserResponsePopulate>

    @POST("recetas")
    fun uploadRecipe(
        @Body recipe: Recipe
    ): Call<RecetasPopulateResponse>


    @GET("users")
    fun getUser(
        @Query("filters[id]") id: String
    ): Call<UserResponse>

    @PUT("users/{id}")
    fun updateUser(
        @Path("id") userId: Int, @Body userData: User
    ): Call<UserResponsePopulate>


}