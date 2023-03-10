package eu.euromov.activemotiv

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent()
        serviceIntent.setClass(context, PopUpService::class.java)
        context.startForegroundService(serviceIntent)
    }
}