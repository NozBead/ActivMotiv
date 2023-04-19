package eu.euromov.activmotiv.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.euromov.activmotiv.R

@Composable
fun Settings(images : List<ImageBitmap>) {
    Page("Photos") {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
            items(images) {
                Image(
                    bitmap = it,
                    modifier = Modifier.size(150.dp),
                    contentScale = ContentScale.Crop,
                    contentDescription = stringResource(id = R.string.wallpaper_desc)
                )
            }
        }
    }
}