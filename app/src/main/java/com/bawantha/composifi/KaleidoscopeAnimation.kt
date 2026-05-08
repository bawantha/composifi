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
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun KaleidoscopeAnimation(modifier: Modifier = Modifier) {
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
        val center = Offset(size.width / 2f, size.height / 2f)
        val maxRadius = size.minDimension / 2f

        val segments = 12
        val layers = 6

        for (layer in 0 until layers) {
            val layerScale = (sin(time + layer) * 0.2f + 0.8f)
            val radius = maxRadius * (layer + 1).toFloat() / layers * layerScale
            val layerRotation = time * if (layer % 2 == 0) 1f else -1f

            val hue = (time * 40f + layer * (360f / layers)) % 360f
            val color = Color.hsv(hue, 0.8f, 0.9f)

            for (i in 0 until segments) {
                val angle1 = (i.toFloat() / segments) * 2f * PI.toFloat() + layerRotation
                val angle2 = ((i + 1).toFloat() / segments) * 2f * PI.toFloat() + layerRotation

                val p1 = Offset(
                    x = center.x + radius * cos(angle1),
                    y = center.y + radius * sin(angle1)
                )

                val p2 = Offset(
                    x = center.x + radius * cos(angle2),
                    y = center.y + radius * sin(angle2)
                )

                drawLine(
                    color = color,
                    start = p1,
                    end = p2,
                    strokeWidth = 4f,
                )

                if (layer > 0) {
                    val prevRadius = maxRadius * layer.toFloat() / layers * (sin(time + layer - 1) * 0.2f + 0.8f)
                    val prevRotation = time * if ((layer - 1) % 2 == 0) 1f else -1f
                    val prevAngle1 = (i.toFloat() / segments) * 2f * PI.toFloat() + prevRotation

                    val p0 = Offset(
                        x = center.x + prevRadius * cos(prevAngle1),
                        y = center.y + prevRadius * sin(prevAngle1)
                    )

                    drawLine(
                        color = color,
                        start = p0,
                        end = p1,
                        strokeWidth = 2f,
                    )
                }
            }
        }
    }
}
