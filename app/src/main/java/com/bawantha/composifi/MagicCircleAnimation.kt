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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MagicCircleAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val path = remember { Path() }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val center = Offset(width / 2f, height / 2f)
        val radius = size.minDimension * 0.45f

        val color = Color(0xFF00FFCC)

        // Outer rotating dashed circle
        rotate(degrees = time * 45f, pivot = center) {
            drawCircle(
                color = color,
                radius = radius,
                center = center,
                style = Stroke(
                    width = 4f,
                    pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
                ),
                alpha = 0.8f
            )
        }

        // Inner reverse rotating circle
        rotate(degrees = -time * 60f, pivot = center) {
            drawCircle(
                color = Color(0xFFFF007F),
                radius = radius * 0.8f,
                center = center,
                style = Stroke(
                    width = 2f,
                    pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 30f), 0f)
                ),
                alpha = 0.7f
            )

            // Hexagon inside
            path.reset()
            for (i in 0..6) {
                val angle = i * (PI / 3).toFloat()
                val x = center.x + radius * 0.8f * cos(angle)
                val y = center.y + radius * 0.8f * sin(angle)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(path = path, color = color, style = Stroke(width = 2f), alpha = 0.6f)
        }

        // Center spinning star
        rotate(degrees = time * 90f, pivot = center) {
            path.reset()
            val starRadius = radius * 0.4f
            for (i in 0..10) {
                val angle = i * (PI / 5).toFloat()
                val r = if (i % 2 == 0) starRadius else starRadius * 0.4f
                val x = center.x + r * cos(angle)
                val y = center.y + r * sin(angle)
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(path = path, color = Color(0xFFFFFF00), style = Stroke(width = 2f), alpha = 0.9f)
        }
    }
}
