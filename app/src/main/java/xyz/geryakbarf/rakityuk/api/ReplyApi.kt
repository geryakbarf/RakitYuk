package xyz.geryakbarf.rakityuk.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import xyz.geryakbarf.rakityuk.models.BaseModels

interface ReplyApi {
    @FormUrlEncoded
    @POST("post_comment.php")
    fun reply(
        @Field("id") id: String,
        @Field("user") user: String,
        @Field("komen") komen: String
    ): Call<BaseModels>
}