package com.example.articulinarytfg

import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("Recetas?populate=*&pagination[limit]=80")
    fun getRecetasPopulateResponse(
    ): Call<RecetasPopulateResponse>


    @GET("Recetas?populate=*&pagination[limit]=80")
    fun getRecetasPopulateResponseFilter(
        @Query("filters[Titulo][$" + "containsi]") searchTerm: String
    ): Call<RecetasPopulateResponse>

    @GET("Users")
    fun getUser(
    ): Call<UserResponse>

    @GET("Users?populate=*")
    fun getUsersPopulateResponsebyUsername(
        @Query("filters[id]") id: Int
    ): Call<UserResponsePopulate>

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
    /*
    @POST("users")
    fun registerUser(
        @Body user: User
    ): Call<UserResponsePopulate>
    */


    @POST("recetas")
    fun uploadRecipe(
        @Body recipe: Recipe
    ): Call<RecetasPopulateResponse>

    //Registrar Usuario
    @POST("auth/local/register")
    fun registerUser(
        @Body request: RegisterData
    ): Call<RegisterResponse>

    data class RegisterResponse(
        val jwt: String,
        val user: User
    ) {
        data class User(
            val blocked: Boolean,
            val confirmed: Boolean,
            val createdAt: String,
            val email: String,
            val id: Int,
            val provider: String,
            val updatedAt: String,
            val username: String
        )
    }

    data class RegisterData(
        val email: String,
        val password: String,
        val username: String
    )

    //Logear Usuario
    @POST("auth/local")
    fun login(
        @Body credentials: LoginCredentials
    ): Call<LoginResponse>

    data class LoginCredentials(
        val identifier: String,
        val password: String
    )

    data class LoginResponse(
        val jwt: String
    )


    @PUT("recetas/{postId}")
    fun updateLikesId(
        @Path("postId") postId: Int,
        @Body likesId: LikesIdOb
    ): Call<RecetasResponse>

    data class LikesIdOb(
        val likesID: String
    )

    @GET("users")
    fun getUser(
        @Query("filters[id]") id: String
    ): Call<UserResponse>

    @GET("users")
    fun getUserbyUserName(
        @Query("filters[username]") username: String
    ): Call<UserResponse>

    @PUT("users/{id}")
    fun updateUser(
        @Path("id") userId: Int, @Body userData: User
    ): Call<UserResponsePopulate>


}