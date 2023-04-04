package eu.euromov.activmotiv.popup

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
import eu.euromov.activmotiv.ui.theme.ActivMotivTheme
import eu.euromov.activmotiv.R
import eu.euromov.activmotiv.client.UploadWorker
import eu.euromov.activmotiv.database.SaveWorker

class ImagesActivity : ComponentActivity() {
    private var exposureTime : Long = 0

    private fun startExposureClock() {
        exposureTime = System.currentTimeMillis()
    }

    private fun stopExposureClock() : Long {
        val currentTime = System.currentTimeMillis()
        exposureTime = currentTime - exposureTime
        return currentTime
    }

    private fun scheduleSaveWork(currentTime : Long, exposureTime : Long) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val saveWorkRequest =
            OneTimeWorkRequestBuilder<SaveWorker>()
                .setInputData(workDataOf("time" to currentTime, "exposureTime" to exposureTime))
                .build()
        val uploadWorkRequest =
            OneTimeWorkRequestBuilder<UploadWorker>()
                .setConstraints(constraints)
                .build()
        WorkManager
            .getInstance(applicationContext)
            .beginWith(saveWorkRequest)
            .then(uploadWorkRequest)
            .enqueue()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { finish() }
            ) {
                StackedImages()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startExposureClock()
    }

    override fun onPause() {
        super.onPause()
        val time = stopExposureClock()
        scheduleSaveWork(time, exposureTime)
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