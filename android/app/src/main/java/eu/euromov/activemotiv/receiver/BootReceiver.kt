package eu.euromov.activemotiv.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import eu.euromov.activemotiv.service.PopUpService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val serviceIntent = Intent(context, PopUpService::class.java)
            context.startForegroundService(serviceIntent)
        }
    }
}