package eu.euromov.activemotiv

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.widget.Toast

class PopUpService : Service() {
    private val receiver = ScreenOnReceiver()

    override fun onCreate() {
        Toast.makeText(this, "Create Service", Toast.LENGTH_SHORT).show()
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        applicationContext.registerReceiver(receiver, filter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_DEFAULT).apply {
            description = "Bienvenue le channel de fous"
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val notification: Notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(getText(R.string.notification_title))
            .setContentText(getText(R.string.notification_message))
            .build()

        startForeground(2, notification)
        return START_STICKY
    }

    override fun onDestroy() {
        applicationContext.unregisterReceiver(receiver)
        Toast.makeText(this, "Destroy service", Toast.LENGTH_SHORT).show()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val CHANNEL_ID = "ActiveMotivNotif"
        const val CHANNEL_NAME = "ActiveMotiv"
    }
}