package eu.euromov.activemotiv

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import eu.euromov.activemotiv.ui.theme.LockScreenManipulatorTheme

class MainActivity : ComponentActivity() {
    private fun startPopupService() {
        val serviceIntent = Intent()
        serviceIntent.setClass(baseContext, PopUpService::class.java)
        baseContext.startForegroundService(serviceIntent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startPopupService()

        setContent {
            LockScreenManipulatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    OrientableSplittedImages(this)
                }
            }
        }
    }
}

@Composable
fun Images(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.chien),
        contentScale = ContentScale.Crop,
        modifier = modifier,
        contentDescription = stringResource(id = R.string.wallpaper_desc),
    )
    Image(
        painter = painterResource(id = R.drawable.sport),
        modifier = modifier,
        contentScale = ContentScale.Crop,
        contentDescription = stringResource(id = R.string.wallpaper_desc),
    )
}
@Composable
fun OrientableSplittedImages(activity: Activity) {
    Box(
        modifier = Modifier.fillMaxSize()
            .clickable { activity.finish() }
    ) {
        val orientation = LocalConfiguration.current.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Images(Modifier.weight(1f))
            }
        }
        else {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Images(Modifier.weight(1f))
            }
        }
    }
}
