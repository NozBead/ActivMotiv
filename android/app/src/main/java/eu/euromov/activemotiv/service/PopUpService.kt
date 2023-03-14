package eu.euromov.activemotiv.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import eu.euromov.activemotiv.R
import eu.euromov.activemotiv.receiver.ScreenOnReceiver

class PopUpService : Service() {
    private val receiver = ScreenOnReceiver()

    override fun onCreate() {
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        applicationContext.registerReceiver(receiver, filter)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_DEFAULT).apply {
            description = "ActiveMotiv Notification Channel"
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
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val CHANNEL_ID = "ActiveMotivNotif"
        const val CHANNEL_NAME = "ActiveMotiv"
    }
}