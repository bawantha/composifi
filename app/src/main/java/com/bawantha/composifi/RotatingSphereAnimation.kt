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
fun RotatingSphereAnimation(modifier: Modifier = Modifier) {
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
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = size.minDimension * 0.45f

        val numPoints = 200
        val phi = PI.toFloat() * (3f - kotlin.math.sqrt(5f)) // Golden angle

        // Define rotations
        val rotX = time * 0.5f
        val rotY = time * 0.8f
        val rotZ = time * 0.3f

        // Precompute sines and cosines for rotation
        val cosX = cos(rotX)
        val sinX = sin(rotX)
        val cosY = cos(rotY)
        val sinY = sin(rotY)
        val cosZ = cos(rotZ)
        val sinZ = sin(rotZ)

        // Store points with their projected depth for sorting
        val projectedPoints = mutableListOf<SpherePoint>()

        for (i in 0 until numPoints) {
            val y = 1f - (i.toFloat() / (numPoints - 1f)) * 2f // y goes from 1 to -1
            val r = kotlin.math.sqrt(1f - y * y) // radius at y

            val theta = phi * i // golden angle increment

            val x = cos(theta) * r
            val z = sin(theta) * r

            // Apply rotations
            // Rotate around X
            val y1 = y * cosX - z * sinX
            val z1 = y * sinX + z * cosX

            // Rotate around Y
            val x2 = x * cosY + z1 * sinY
            val z2 = -x * sinY + z1 * cosY

            // Rotate around Z
            val x3 = x2 * cosZ - y1 * sinZ
            val y3 = x2 * sinZ + y1 * cosZ

            // Perspective projection
            val perspective = 2f / (2f - z2) // simplistic perspective

            val projX = centerX + x3 * radius * perspective
            val projY = centerY + y3 * radius * perspective

            // The depth is z2. We sort by z2 ascending (smaller z is further away)
            projectedPoints.add(SpherePoint(projX, projY, z2, perspective))
        }

        // Sort points back-to-front
        projectedPoints.sortBy { it.z }

        for (point in projectedPoints) {
            // Calculate size and opacity based on z-depth
            val pointSize = 3f * point.perspective
            // Map z from [-1, 1] to alpha [0.2, 1.0]
            val alpha = ((point.z + 1f) / 2f * 0.8f + 0.2f).coerceIn(0f, 1f)

            // Choose color based on position or depth
            val color = Color.hsv(
                hue = (point.z + 1f) * 180f % 360f,
                saturation = 0.8f,
                value = 1f,
                alpha = alpha
            )

            drawCircle(
                color = color,
                radius = pointSize,
                center = Offset(point.x, point.y)
            )
        }
    }
}

private data class SpherePoint(val x: Float, val y: Float, val z: Float, val perspective: Float)
