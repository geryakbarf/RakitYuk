package xyz.geryakbarf.rakityuk.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import xyz.geryakbarf.rakityuk.models.LoginModels

interface LoginApi {
    @FormUrlEncoded
    @POST("api_login.php") //Isi dengan nama file php yang telah kita buat tadi
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginModels>
}
