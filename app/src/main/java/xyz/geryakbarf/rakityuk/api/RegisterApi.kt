package xyz.geryakbarf.rakityuk.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import xyz.geryakbarf.rakityuk.models.BaseModels

interface RegisterApi {
    @FormUrlEncoded
    @POST("api_register.php")
    fun register(
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<BaseModels>
}