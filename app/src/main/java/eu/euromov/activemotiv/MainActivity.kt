package eu.euromov.activemotiv

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.euromov.activemotiv.ui.theme.LockScreenManipulatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val serviceIntent = Intent()
        serviceIntent.setClass(baseContext, PopUpService::class.java)
        baseContext.startForegroundService(serviceIntent)
        setContent {
            LockScreenManipulatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    Box(contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                            Image(
                                painter = painterResource(id = R.drawable.wallpaper),
                                contentDescription = stringResource(id = R.string.wallpaper_desc),
                            )
                            Image(
                                painter = painterResource(id = R.drawable.wallpaper),
                                contentDescription = stringResource(id = R.string.wallpaper_desc),
                            )
                        }
                    }
                }
            }
        }
    }
}