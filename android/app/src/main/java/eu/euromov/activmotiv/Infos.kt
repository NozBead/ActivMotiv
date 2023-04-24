package eu.euromov.activmotiv

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Infos() {
    Page {
        Text(
            "Blablabla",
            modifier = Modifier
                .padding(20.dp)
                .weight(0.2F)
        )
    }
}