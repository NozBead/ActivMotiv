package eu.euromov.activemotiv

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.widget.Toast

class StartActivityReciever : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Start activity", Toast.LENGTH_SHORT).show()
        val startIntent = Intent(context, MainActivity::class.java)
        startIntent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(startIntent)
    }
}