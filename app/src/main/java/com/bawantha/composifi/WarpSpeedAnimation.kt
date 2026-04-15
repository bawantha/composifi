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
import androidx.compose.ui.graphics.StrokeCap
import kotlinx.coroutines.isActive
import kotlin.random.Random

@Composable
fun WarpSpeedAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val stars = remember {
        List(200) {
            Star(
                x = Random.nextFloat() * 2f - 1f,
                y = Random.nextFloat() * 2f - 1f,
                z = Random.nextFloat()
            )
        }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f

        // Draw background
        drawRect(color = Color.Black, size = size)

        for (star in stars) {
            val z = ((star.z - time * 0.5f) % 1f + 1f) % 1f

            val sx = star.x / z
            val sy = star.y / z

            val px = centerX + sx * width / 2f
            val py = centerY + sy * height / 2f

            if (px in 0f..width && py in 0f..height) {
                val size = (1f - z) * 4f
                val color = Color.White.copy(alpha = 1f - z)

                val pz = z + 0.05f
                val psx = star.x / pz
                val psy = star.y / pz
                val ppx = centerX + psx * width / 2f
                val ppy = centerY + psy * height / 2f

                drawLine(
                    color = color,
                    start = Offset(ppx, ppy),
                    end = Offset(px, py),
                    strokeWidth = size,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

private class Star(
    val x: Float,
    val y: Float,
    var z: Float
)
