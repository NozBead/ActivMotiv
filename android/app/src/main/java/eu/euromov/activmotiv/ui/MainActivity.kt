package eu.euromov.activmotiv.ui

import android.content.Intent
import android.os.Bundle
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
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import eu.euromov.activmotiv.R
import eu.euromov.activmotiv.service.PopUpService
import eu.euromov.activmotiv.database.UnlockDatabase
import eu.euromov.activmotiv.model.Unlock
import eu.euromov.activmotiv.ui.theme.ActivMotivTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var exposureTime : Long = 0
    private var unlockDatabase : UnlockDatabase? = null

    private fun createDatabase() : UnlockDatabase {
        return Room.databaseBuilder(
            applicationContext,
            UnlockDatabase::class.java, "Unlock Database"
        ).build()
    }

    private fun startPopupService() {
        val serviceIntent = Intent()
        serviceIntent.setClass(baseContext, PopUpService::class.java)
        baseContext.startForegroundService(serviceIntent)
    }

    private fun startExposureClock() {
        exposureTime = System.currentTimeMillis()
    }

    private fun stopExposureClock() {
        val currentTime = System.currentTimeMillis()
        exposureTime = currentTime - exposureTime
        val unlock = Unlock(0, currentTime, exposureTime, false)
        unlockDatabase?.unlockDao()?.insert(unlock)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startPopupService()
        unlockDatabase = createDatabase()

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

        lifecycleScope.launch(Dispatchers.IO) {
            stopExposureClock()
        }
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