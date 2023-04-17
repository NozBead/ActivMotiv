package eu.euromov.activmotiv.client

import eu.euromov.activmotiv.model.Images
import eu.euromov.activmotiv.model.Unlock
import eu.euromov.activmotiv.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.*


interface UploadClient {
    companion object {
        @Volatile
        private var INSTANCE: UploadClient? = null

        fun getClient(baseUrl : String): UploadClient {
            return INSTANCE ?: synchronized(this) {
                val retrofit = Retrofit.Builder()
                    .addConverterFactory(JacksonConverterFactory.create())
                    .baseUrl(baseUrl)
                    .build()
                val instance = retrofit.create(UploadClient::class.java)
                INSTANCE = instance
                instance
            }
        }
    }

    @GET("participant/login")
    fun login(@Header("Authorization") authorization : String): Call<Unit>

    @GET("participant/login")
    fun check(@Header("Cookie") auth : String): Call<Unit>

    @POST("participant")
    fun register(@Body user : User): Call<Unit>

    @POST("unlock")
    fun unlock(@Header("Cookie") auth : String, @Body unlock : Unlock): Call<Unit>

    @GET("image/{type}")
    fun getImages(@Path("type") type : String): Call<Images>

    @GET("res/{type}/{image}")
    fun getImage(@Path("type") type : String, @Path("image") image : String) : Call<ResponseBody>
}