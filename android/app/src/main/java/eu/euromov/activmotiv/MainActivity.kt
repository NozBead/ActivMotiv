package eu.euromov.activmotiv

import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.work.*
import eu.euromov.activmotiv.client.UploadWorker
import eu.euromov.activmotiv.ui.theme.ActivMotivTheme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val serviceIntent = Intent(applicationContext, MainService::class.java)
        applicationContext.startForegroundService(serviceIntent)

//        AccountManager.get(applicationContext)
//            .addAccount(
//                resources.getString(R.string.accountType),
//                "",
//                null,
//                null,
//                this,
//                null,
//                null)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val uploadWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setConstraints(constraints)
            .build()

        setContent {
            ActivMotivTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { WorkManager
                            .getInstance(applicationContext)
                            .enqueue(uploadWorkRequest) }
                ) {
                }
            }
        }
    }
}