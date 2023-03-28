package eu.euromov.activmotiv.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.work.*
import eu.euromov.activmotiv.R
import eu.euromov.activmotiv.service.PopUpService
import eu.euromov.activmotiv.data.model.Unlock
import eu.euromov.activmotiv.ui.theme.ActivMotivTheme
import eu.euromov.activmotiv.work.SaveWorker
import java.time.Instant

class ImagesActivity : ComponentActivity() {
    private var exposureTime : Long = 0

    private fun startPopupService() {
        val serviceIntent = Intent()
        serviceIntent.setClass(baseContext, PopUpService::class.java)
        baseContext.startForegroundService(serviceIntent)
    }

    private fun startExposureClock() {
        exposureTime = System.currentTimeMillis()
    }

    private fun stopExposureClock() : Unlock {
        val currentTime = System.currentTimeMillis()
        exposureTime = currentTime - exposureTime
        return Unlock(Instant.ofEpochMilli(currentTime), exposureTime, false)
    }

    private fun scheduleSaveWork(unlock : Unlock) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val saveWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<SaveWorker>()
                .setConstraints(constraints)
                .setInputData(workDataOf("Unlock" to unlock))
                .build()
        WorkManager
            .getInstance(applicationContext)
            .enqueue(saveWorkRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startPopupService()

        setContent {
            ActivMotivTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { finish() }
                ) {
                   StackedImages()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startExposureClock()
    }

    override fun onPause() {
        super.onPause()
        val unlock = stopExposureClock()
        scheduleSaveWork(unlock)
    }
}

@Composable
fun StackedImages() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = painterResource(id = R.drawable.chien),
            contentScale = ContentScale.Crop,
            modifier = Modifier.weight(1f),
            contentDescription = stringResource(id = R.string.wallpaper_desc),
        )
        Image(
            painter = painterResource(id = R.drawable.sport),
            modifier = Modifier.weight(1f),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(id = R.string.wallpaper_desc),
        )
    }
}