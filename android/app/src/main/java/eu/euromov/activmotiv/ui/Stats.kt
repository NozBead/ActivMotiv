package eu.euromov.activmotiv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.euromov.activmotiv.R
import eu.euromov.activmotiv.model.Stats

@Composable
fun Stat(title : String, value : Float , unit : String, modifier : Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier.padding(10.dp),
        elevation = CardDefaults.elevatedCardElevation()
    ){
        Column(
            modifier = Modifier.padding(10.dp)
        ){
            Text(
                fontSize = 20.sp,
                text = "$title:"
            )
            Text(
                fontSize = 20.sp,
                text = "$value $unit"
            )
        }
    }
}

@Composable
fun Stats(stats : Stats) {
    Page(stringResource(id = R.string.stats)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        )
        {
            Stat(
                stringResource(id = R.string.unlock_number),
                stats.unlockNumber.toFloat(),
                "",
                modifier = Modifier.weight(1f),
            )

            Stat(
                stringResource(id = R.string.total_exposure),
                stats.totalExposure.toFloat() / 1000f,
                stringResource(id = R.string.seconds),
                modifier = Modifier.weight(1f)
            )
        }
        OutlinedCard(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            elevation = CardDefaults.elevatedCardElevation()
        ) {

        }
    }
}