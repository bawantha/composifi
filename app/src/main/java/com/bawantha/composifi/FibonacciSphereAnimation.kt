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
import kotlin.math.sqrt

@Composable
fun FibonacciSphereAnimation(modifier: Modifier = Modifier) {
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
        val cx = width / 2f
        val cy = height / 2f
        val maxRadius = size.minDimension * 0.45f

        val points = 200
        val phi = PI * (3.0 - sqrt(5.0))

        // z-sorting
        val projectedPoints = mutableListOf<SpherePoint>()

        for (i in 0 until points) {
            val y = 1f - (i / (points - 1f)) * 2f
            val radiusAtY = sqrt(1f - y * y)

            val theta = phi * i + time

            val x = (cos(theta) * radiusAtY).toFloat()
            val z = (sin(theta) * radiusAtY).toFloat()

            projectedPoints.add(SpherePoint(x, y, z, i))
        }

        // Sort by z to draw back points first
        projectedPoints.sortBy { it.z }

        for (point in projectedPoints) {
            val perspective = 2f / (2f + point.z)
            val projX = cx + point.x * maxRadius * perspective
            val projY = cy + point.y * maxRadius * perspective

            val pointSize = (perspective * 3f).coerceAtLeast(0.5f)
            val colorAlpha = ((point.z + 1f) / 2f).coerceIn(0.2f, 1f)

            val hue = ((time * 50f + point.index) % 360f).coerceAtLeast(0f)
            val color = Color.hsv(hue, 0.8f, 1f, colorAlpha)

            drawCircle(
                color = color,
                radius = pointSize,
                center = Offset(projX, projY)
            )
        }
    }
}

private data class SpherePoint(val x: Float, val y: Float, val z: Float, val index: Int)
