package eu.euromov.activmotiv.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import eu.euromov.activmotiv.ui.ImagesActivity

class ScreenOnReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_SCREEN_ON) {
            val startIntent = Intent(context, ImagesActivity::class.java)
            startIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(startIntent)
        }
    }
}