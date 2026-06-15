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
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GeometricTunnelAnimation(modifier: Modifier = Modifier) {
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
    val numShapes = 15
    val sides = 6

    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val maxRadius = size.maxDimension * 0.8f

        val shapes = (0 until numShapes).map { i ->
            val rawProgress = (i.toFloat() / numShapes + time * 0.4f) % 1f
            val progress = rawProgress * rawProgress * rawProgress
            val radius = maxRadius * progress
            val rotation = time * 0.5f + rawProgress * PI.toFloat() * 0.5f
            val vertices = Array(sides) { j ->
                val angle = rotation + j * (2 * PI / sides).toFloat()
                Offset(cx + radius * cos(angle), cy + radius * sin(angle))
            }
            TunnelShapeData(rawProgress, progress, vertices)
        }.sortedBy { it.rawProgress }

        for (index in shapes.indices) {
            val shape = shapes[index]

            path.reset()
            for (j in 0 until sides) {
                if (j == 0) path.moveTo(shape.vertices[j].x, shape.vertices[j].y)
                else path.lineTo(shape.vertices[j].x, shape.vertices[j].y)
            }
            path.close()

            val alpha = (shape.rawProgress * 3f).coerceAtMost(1f) * (1f - shape.rawProgress)
            val hue = (time * 30f + shape.rawProgress * 120f) % 360f
            val color = Color.hsv(hue, 0.8f, 1f, alpha = alpha.coerceIn(0f, 1f))

            val strokeWidth = 1f + shape.progress * 8f

            drawPath(
                path = path,
                color = color,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            if (index > 0) {
                val prevShape = shapes[index - 1]
                for (j in 0 until sides) {
                    drawLine(
                        color = color.copy(alpha = alpha * 0.5f),
                        start = prevShape.vertices[j],
                        end = shape.vertices[j],
                        strokeWidth = strokeWidth * 0.5f
                    )
                }
            }
        }
    }
}

private data class TunnelShapeData(
    val rawProgress: Float,
    val progress: Float,
    val vertices: Array<Offset>
)
