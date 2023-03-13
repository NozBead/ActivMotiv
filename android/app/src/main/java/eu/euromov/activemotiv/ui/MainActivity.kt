package eu.euromov.activemotiv.ui

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
import eu.euromov.activemotiv.PopUpService
import eu.euromov.activemotiv.R
import eu.euromov.activemotiv.ui.theme.ActiveMotivTheme

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
            ActiveMotivTheme {
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