package eu.euromov.activmotiv.data.client

import eu.euromov.activmotiv.data.model.Unlock
import eu.euromov.activmotiv.data.model.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface UploadClient {
    companion object {
        @Volatile
        private var INSTANCE: UploadClient? = null

        fun getClient(baseUrl : String): UploadClient {
            return INSTANCE ?: synchronized(this) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .build();
                val instance = retrofit.create(UploadClient::class.java)
                INSTANCE = instance
                instance
            }
        }
    }

    @GET("participant/login")
    fun login(@Header("Authorization") authorization : String): Call<Unit>

    @POST("participant")
    fun register(@Body user : User): Call<Unit>

    @POST("unlock")
    fun unlock(@Header("Cookie") auth : String, @Body unlock : Unlock): Call<Unit>
}