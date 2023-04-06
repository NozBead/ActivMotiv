package eu.euromov.activmotiv.popup

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
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
import eu.euromov.activmotiv.client.UploadWorker
import eu.euromov.activmotiv.database.SaveWorker

class ImagesActivity : ComponentActivity() {
    private var exposureTime : Long = 0
    private var skip = false

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
        Log.i("state","create")
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

    override fun onStart() {
        super.onStart()
        Log.i("state","start")
    }

    override fun onRestart() {
        super.onRestart()
        skip = true
        Log.i("state","restart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("state", "resume")
        startExposureClock()
    }

    override fun onPause() {
        super.onPause()
        Log.i("state", "pause")

    }

    override fun onStop() {
        super.onStop()
        Log.i("state", "stop")
        if (!skip) {
            val time = stopExposureClock()
            scheduleSaveWork(time, exposureTime)
        }
        skip = false
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("state", "destroy")
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