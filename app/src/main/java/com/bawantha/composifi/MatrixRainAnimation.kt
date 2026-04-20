package com.bawantha.composifi

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.isActive
import kotlin.random.Random

@Composable
fun MatrixRainAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val textMeasurer = rememberTextMeasurer()
    val characters = remember { "ｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜﾝ0123456789".toCharArray() }

    val columns = 20
    val drops = remember { FloatArray(columns) { Random.nextFloat() * -1000f } }
    val speeds = remember { FloatArray(columns) { Random.nextFloat() * 250f + 100f } }
    val lengths = remember { IntArray(columns) { Random.nextInt(5, 20) } }

    Canvas(modifier = modifier) {
        // Read animated state variable 'time' to ensure draw phase invalidation
        val currentTime = time
        val width = size.width
        val height = size.height
        val columnWidth = width / columns
        val charHeight = 24f

        for (i in 0 until columns) {
            val x = i * columnWidth
            val y = drops[i]

            for (j in 0 until lengths[i]) {
                val dropY = y - j * charHeight
                // Skip rendering if outside canvas bounds to improve performance
                if (dropY < -charHeight || dropY > height) continue

                // Pick a pseudo-random character based on current time and position
                val charIndex = (i * 31 + j * 17 + (currentTime * 10).toInt()) % characters.size
                val char = characters[charIndex].toString()

                // Head of the drop is white, rest is green fading out
                val color = if (j == 0) {
                    Color.White
                } else {
                    Color(0xFF00FF41).copy(alpha = 1f - (j.toFloat() / lengths[i]))
                }

                drawText(
                    textMeasurer = textMeasurer,
                    text = char,
                    topLeft = Offset(x, dropY),
                    style = TextStyle(color = color, fontSize = 16.sp)
                )
            }

            // Update drop position for next frame
            drops[i] += speeds[i] * 0.016f

            // Reset drop to top if it has fallen completely off screen
            if (drops[i] - lengths[i] * charHeight > height) {
                drops[i] = Random.nextFloat() * -200f - 50f
                speeds[i] = Random.nextFloat() * 250f + 100f
                lengths[i] = Random.nextInt(5, 20)
            }
        }
    }
}
