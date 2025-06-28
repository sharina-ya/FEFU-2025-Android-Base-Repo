package co.feip.fefu2025.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RatingBarChart(
    ratings: Map<Int, Int>,
    modifier: Modifier = Modifier
) {
    val maxCount = ratings.values.maxOrNull() ?: 1

    Column(modifier = modifier) {
        Text("Оценки пользователей", modifier = Modifier.padding(bottom = 8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        ) {
            ratings.entries.sortedBy { it.key }.forEach { (rating, count) ->
                val heightRatio = if (maxCount > 0) count.toFloat() / maxCount else 0f
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height((heightRatio * 150).dp)
                            .background(Color.Blue)
                    )
                    Text("$rating", modifier = Modifier.padding(top = 4.dp))
                    Text("$count", modifier = Modifier.padding(top = 2.dp), fontSize = 10.sp)
                }
            }
        }
    }
}