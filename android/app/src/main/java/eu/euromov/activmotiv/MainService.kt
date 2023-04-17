package eu.euromov.activmotiv

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import androidx.work.*
import eu.euromov.activmotiv.client.UploadWorker
import eu.euromov.activmotiv.popup.PresenceReceiver
import eu.euromov.activmotiv.popup.SyncImageWorker
import java.util.concurrent.TimeUnit

class MainService : Service() {
    private val receiver = PresenceReceiver()

    override fun onCreate() {
        val filter = IntentFilter(Intent.ACTION_USER_PRESENT)
        applicationContext.registerReceiver(receiver, filter)
    }

    private fun scheduleWorkers() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncImageWorker>(3, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()
        val uploadWorkRequest = PeriodicWorkRequestBuilder<UploadWorker>(20, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        WorkManager
            .getInstance(applicationContext)
            .enqueueUniquePeriodicWork("Upload to server", ExistingPeriodicWorkPolicy.UPDATE, uploadWorkRequest)
        WorkManager
            .getInstance(applicationContext)
            .enqueueUniquePeriodicWork("Sync images", ExistingPeriodicWorkPolicy.UPDATE, syncWorkRequest)
    }

    private fun startNotification() {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_DEFAULT).apply {
            description = "ActivMotiv Notification Channel"
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val notification: Notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(getText(R.string.notification_title))
            .setContentText(getText(R.string.notification_message))
            .build()

        startForeground(2, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scheduleWorkers()
        startNotification()
        return START_STICKY
    }

    override fun onDestroy() {
        applicationContext.unregisterReceiver(receiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val CHANNEL_ID = "ActivMotivNotif"
        const val CHANNEL_NAME = "ActivMotiv"
    }
}