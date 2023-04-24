package eu.euromov.activmotiv.popup

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import eu.euromov.activmotiv.R
import eu.euromov.activmotiv.client.UploadWorker
import eu.euromov.activmotiv.database.SaveWorker
import eu.euromov.activmotiv.database.UnlockDatabase
import eu.euromov.activmotiv.model.ImageType
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
            val imageDao = UnlockDatabase.getDatabase(applicationContext).imageDao()
            val positives = imageDao.getAllOfType(ImageType.POSITIVE)
            val sports = imageDao.getAllOfType(ImageType.SPORT)

            val rng = Random.Default
            val positiveFile = positives[rng.nextInt(positives.size)].file
            val positive = BitmapFactory.decodeByteArray(positiveFile, 0, positiveFile.size)
            val sportFile = sports[rng.nextInt(sports.size)].file
            val sport = BitmapFactory.decodeByteArray(sportFile, 0, sportFile.size)
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