package eu.euromov.activmotiv.client

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientCallback(val action : (Response<Unit>) -> Unit): Callback<Unit> {

    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
        action(response)
    }

    override fun onFailure(call: Call<Unit>, t: Throwable) {
        Log.e("Client", t.stackTraceToString())
    }
}