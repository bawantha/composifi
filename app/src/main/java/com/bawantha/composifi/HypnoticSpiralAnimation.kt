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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun HypnoticSpiralAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val center = Offset(width / 2f, height / 2f)
        val maxRadius = size.minDimension / 2f

        val rings = 8
        for (i in 0 until rings) {
            val radius = maxRadius * (i + 1) / rings
            val phaseOffset = i * (PI / 4).toFloat()
            val scale = 1f + 0.2f * sin(time * 2f + phaseOffset).toFloat()

            // Generate color without Color.hsv to be safe, or try Color.hsv
            val hue = (time * 50f + i * 20f) % 360f
            val color = Color.hsv(hue, 0.8f, 1f)

            rotate(degrees = time * 30f * if (i % 2 == 0) 1f else -1f, pivot = center) {
                drawCircle(
                    color = color,
                    radius = radius * scale,
                    center = center,
                    style = Stroke(width = 8f, cap = StrokeCap.Round),
                    alpha = 0.8f
                )
            }
        }
    }
}
