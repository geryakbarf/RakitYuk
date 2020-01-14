package xyz.geryakbarf.rakityuk.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import xyz.geryakbarf.rakityuk.models.ThreadModels

interface ThreadApi {
    @FormUrlEncoded
    @POST("detail_thread.php")
    fun getDetail(
        @Field("id") id: String
    ): Call<ThreadModels>
}