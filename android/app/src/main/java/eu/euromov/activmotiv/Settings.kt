package eu.euromov.activmotiv

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun Settings(onGetImages: () -> List<ImageBitmap>, onSelectImage: (imageId: Int) -> Unit) {
    TitledPage("Photos") {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            itemsIndexed(onGetImages()) { index, item ->
                Image(
                    bitmap = item,
                    modifier = Modifier
                        .size(150.dp)
                        .clickable { onSelectImage(index) },
                    contentScale = ContentScale.Crop,
                    contentDescription = stringResource(id = R.string.wallpaper_desc)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSettings(image : ImageBitmap) {
    Page({}) {
        Image(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .weight(1F),
            bitmap = image,
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(id = R.string.wallpaper_desc)
        )
        Row(
            modifier = Modifier
                .padding(20.dp)
                .weight(0.2F)
        ) {
            var faved by remember { mutableStateOf(false) }
            Icon(
                painterResource(id = R.drawable.comment),
                "fav",
                modifier = Modifier
                    .weight(1F)
                    .fillMaxSize()
            )
            Icon(
                if (faved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                "fav",
                modifier = Modifier
                    .weight(1F)
                    .fillMaxSize()
                    .clickable { faved = !faved }
            )
        }
        var text by remember { mutableStateOf("") }
        TextField(
            text,
            {text = it},
            label = { Text(stringResource(id = R.string.comment)) },
            modifier = Modifier
                .padding(20.dp)
                .weight(0.2F)
        )
    }
}