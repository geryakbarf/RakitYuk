package xyz.geryakbarf.rakityuk.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import xyz.geryakbarf.rakityuk.models.BaseModels

interface DownComment {
    @FormUrlEncoded
    @POST("dislike_comment.php")
    fun dislike(
        @Field("id") id: String,
        @Field("user") user: String
    ): Call<BaseModels>
}