package eu.euromov.activmotiv.client.auth

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AccountService : Service() {
    override fun onBind(intent: Intent): IBinder {
        return ClientAuthenticator(context = applicationContext).iBinder
    }
}