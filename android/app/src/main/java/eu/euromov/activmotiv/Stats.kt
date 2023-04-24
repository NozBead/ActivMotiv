package eu.euromov.activmotiv

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.euromov.activmotiv.model.Stats
import eu.euromov.activmotiv.model.UnlockDay

@Composable
fun Stat(title : String, value : String , unit : String, colors: CardColors, modifier : Modifier = Modifier) {
    Card(
        colors = colors,
        modifier = modifier.padding(10.dp)
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

fun getMondayDayOfWeek(sundayDayOfWeek : Int) : Int = (sundayDayOfWeek - 1).mod(7)
val daysLabel = arrayOf("Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim")

@OptIn(ExperimentalTextApi::class)
@Composable
fun Stats(onGetInfo: () -> Unit, onGetStats: () -> Stats, onGetUnlocks: () -> List<UnlockDay>) {
    TitledPage(
        stringResource(id = R.string.stats),
        onGetInfo
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        )
        {
            val stats = remember { onGetStats() }
            Stat(
                stringResource(id = R.string.unlock_number),
                stats.unlockNumber.toString(),
                "",
                CardDefaults.cardColors(
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.onSecondaryContainer
                ),
                modifier = Modifier
                    .weight(1f),
            )

            Stat(
                stringResource(id = R.string.total_exposure),
                (stats.totalExposure.toFloat() / 1000f).toString(),
                stringResource(id = R.string.seconds),
                CardDefaults.cardColors(
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier
                    .weight(1f)
            )
        }
        val unlocks = remember { onGetUnlocks() }
        val textMeasurer = rememberTextMeasurer()
        val color = MaterialTheme.colorScheme.onBackground
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .weight(10F)
                .padding(10.dp)
        ) {
            val unlockDays = IntArray(7)
            var max = 0F
            for (unlock in unlocks) {
                unlockDays[getMondayDayOfWeek(unlock.dayOfWeek)] = unlock.numberOfUnlocks
                if (max < unlock.numberOfUnlocks) {
                    max = unlock.numberOfUnlocks.toFloat()
                }
            }

            val xOffset = textMeasurer
                .measure(
                AnnotatedString(max.toString()),
                TextStyle(color))
                .size.width
            val yOffset = 100F
            val axisBottom = size.height - yOffset
            val axisLeft = xOffset.toFloat()

            val xScale : Float = (size.width - xOffset) / unlockDays.size

            val barWidth = 100F
            val barOffset = (xScale - barWidth) / 2

            unlockDays
                .forEachIndexed { i, number ->
                    val offset = Offset(i * xScale, (1 - (number / max)) * axisBottom)
                    val lineX = offset.x + xOffset + barOffset
                    if (number > 0) {
                        drawRect(
                            color,
                            topLeft = Offset(lineX, offset.y),
                            size = Size(barWidth, axisBottom - offset.y)
                        )
                    }
                    val measuredText = textMeasurer.measure(
                        AnnotatedString(daysLabel[i]),
                        TextStyle(color),
                    )
                    drawText(
                        measuredText,
                        topLeft = Offset(lineX + ((barWidth - measuredText.size.width) / 2) , axisBottom + 10)
                    )
                }

            var step = max.toInt() / 5
            step = if (step == 0) 1 else step
            for (i in 0..max.toInt() step step) {
                val lineY = (1 - (i / max)) * axisBottom
                val measuredText = textMeasurer.measure(
                    AnnotatedString(i.toString()),
                    TextStyle(color),
                )
                drawText(
                    measuredText,
                    topLeft = Offset(0F, lineY - (measuredText.size.height / 2))
                )
                drawLine(
                    color,
                    Offset(axisLeft, lineY),
                    Offset(size.width, lineY),
                    2F,
                    StrokeCap.Round
                )
            }

            drawLine(
                color,
                Offset(axisLeft, 0F),
                Offset(axisLeft, axisBottom),
                5F,
                StrokeCap.Round
            )
            drawLine(
                color,
                Offset(axisLeft, axisBottom),
                Offset(size.width, axisBottom),
                5F,
                StrokeCap.Round
            )
        }
        Text(
            "Nombre d'expositions par jour cette semaine",
            modifier = Modifier.weight(1F)
        )
    }
}