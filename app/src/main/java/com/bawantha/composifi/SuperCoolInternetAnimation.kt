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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SuperCoolInternetAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val particleCount = 60
    val rings = 5

    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val maxRadius = size.minDimension / 2f

        for (ring in 1..rings) {
            val ringRadius = maxRadius * (ring.toFloat() / rings)
            val ringTimeOffset = time * (1f + ring * 0.2f)

            for (i in 0 until particleCount) {
                val angle = (i.toFloat() / particleCount) * 2 * PI.toFloat() + ringTimeOffset * (if (ring % 2 == 0) 1 else -1)

                val x = cx + cos(angle) * ringRadius
                val y = cy + sin(angle) * ringRadius

                val hue = (time * 50f + ring * 360f / rings + i * 360f / particleCount) % 360f
                val color = Color.hsv(hue, 0.8f, 1f)

                val particleSize = (sin(time * 3f + i * 0.2f) + 1f) * 2f + 2f

                drawCircle(
                    color = color,
                    radius = particleSize,
                    center = Offset(x, y)
                )
            }
        }
    }
}
