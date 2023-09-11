package eu.euromov.activmotiv

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import eu.euromov.activmotiv.model.Image

@Composable
fun ImageItem(index: Int, image: Image, onSelectImage: (index: Int) -> Unit) {
    Box (
        modifier = Modifier.size(150.dp)
    ) {
        Image(
            bitmap = BitmapFactory.decodeByteArray(image.file, 0, image.file.size).asImageBitmap(),
            modifier = Modifier
                .fillMaxSize()
                .clickable { onSelectImage(index) },
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(id = R.string.wallpaper_desc)
        )
        if (image.favorite) {
            Icon(
                Icons.Filled.Favorite,
                "fav",
                tint = Color(0xFFCE0F0F),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(5.dp)
            )
        }
    }
}

@Composable
fun Settings(onGetInfo: () -> Unit, onGetImages: () -> List<Image>, onSelectImage: (imageId: Int) -> Unit) {
    TitledPage(
        "Photos",
        onGetInfo
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            itemsIndexed(onGetImages()) { index, item ->
                ImageItem(index, item, onSelectImage)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ImageSettings(onGetInfo: () -> Unit, onUpdateImage: (image: Image) -> Unit, image : Image) {
    Page(onGetInfo) {
        Image(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .weight(1F),
            bitmap = BitmapFactory.decodeByteArray(image.file, 0, image.file.size).asImageBitmap(),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(id = R.string.wallpaper_desc)
        )
        Row(
            modifier = Modifier
                .padding(20.dp)
                .weight(0.2F)
        ) {
            var faved by remember { mutableStateOf(image.favorite) }
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
                    .clickable {
                        faved = !faved
                        image.favorite = faved
                        onUpdateImage(image)
                    }
            )
        }
        var text by remember { mutableStateOf(image.comment ?: "") }
        val keyboardController = LocalSoftwareKeyboardController.current
        TextField(
            text,
            {
                text = it
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    image.comment = text
                    onUpdateImage(image)
                    keyboardController?.hide()
                }
            ),
            label = { Text(stringResource(id = R.string.comment)) },
            modifier = Modifier
                .padding(20.dp)
        )
    }
}