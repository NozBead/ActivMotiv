package eu.euromov.activmotiv.popup

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.work.*
import eu.euromov.activmotiv.R
import eu.euromov.activmotiv.client.UploadWorker
import eu.euromov.activmotiv.database.SaveWorker
import java.io.File
import kotlin.random.Random

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
        setContent {
            val positives = File(applicationContext.filesDir, "positive").listFiles()
            val sports = File(applicationContext.filesDir, "sport").listFiles()

            val rng = Random.Default
            val positive = BitmapFactory.decodeStream(positives[rng.nextInt(positives.size)].inputStream())
            val sport = BitmapFactory.decodeStream(sports[rng.nextInt(sports.size)].inputStream())
            rng.nextInt()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { finish() }
            ) {
                StackedImages(
                    positive.asImageBitmap(),
                    sport.asImageBitmap()
                )
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        skip = true
    }

    override fun onResume() {
        super.onResume()
        startExposureClock()
    }

    override fun onStop() {
        super.onStop()
        if (!skip) {
            val time = stopExposureClock()
            scheduleSaveWork(time, exposureTime)
        }
        skip = false
    }
}

@Composable
fun StackedImages(positive : ImageBitmap, sport : ImageBitmap) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            bitmap = positive,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentDescription = stringResource(id = R.string.wallpaper_desc),
        )
        Image(
            bitmap = sport,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(id = R.string.wallpaper_desc),
        )
    }
}