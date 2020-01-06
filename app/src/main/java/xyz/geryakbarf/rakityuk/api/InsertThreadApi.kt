package xyz.geryakbarf.rakityuk.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import xyz.geryakbarf.rakityuk.models.BaseModels

interface InsertThreadApi {
    @FormUrlEncoded
    @POST("create_thread.php") //Isi dengan nama file php yang telah kita buat tadi
    fun insertThread(
        @Field("judul") judul: String,
        @Field("isi") isi: String,
        @Field("username") username: String
    ): Call<BaseModels>
}