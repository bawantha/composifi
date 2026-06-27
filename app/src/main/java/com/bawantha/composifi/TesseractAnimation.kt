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
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TesseractAnimation(modifier: Modifier = Modifier) {
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
        val cx = size.width / 2f
        val cy = size.height / 2f
        val minDim = minOf(size.width, size.height)
        val s1 = minDim * 0.4f * (0.8f + 0.2f * sin(time * 2f)) // Outer cube pulses
        val s2 = minDim * 0.2f * (0.8f + 0.2f * cos(time * 2f)) // Inner cube pulses

        // 3D rotation angles
        val angleX = time * 0.7f
        val angleY = time * 1.1f
        val angleZ = time * 0.5f

        // Helper function to project 3D point to 2D
        fun project(x: Float, y: Float, z: Float, scale: Float): Offset {
            // Rotate X
            var ry = y * cos(angleX) - z * sin(angleX)
            var rz = y * sin(angleX) + z * cos(angleX)
            val rx1 = x

            // Rotate Y
            val rx2 = rx1 * cos(angleY) + rz * sin(angleY)
            rz = -rx1 * sin(angleY) + rz * cos(angleY)
            val ry2 = ry

            // Rotate Z
            val rx3 = rx2 * cos(angleZ) - ry2 * sin(angleZ)
            val ry3 = rx2 * sin(angleZ) + ry2 * cos(angleZ)

            // Simple perspective projection
            val distance = 2.5f
            val zProjected = 1f / (distance - rz)

            return Offset(
                cx + rx3 * scale * zProjected,
                cy + ry3 * scale * zProjected
            )
        }

        // Define 8 vertices of a cube (values -1 or 1)
        val vertices = listOf(
            Triple(-1f, -1f, -1f), Triple(1f, -1f, -1f),
            Triple(1f, 1f, -1f), Triple(-1f, 1f, -1f),
            Triple(-1f, -1f, 1f), Triple(1f, -1f, 1f),
            Triple(1f, 1f, 1f), Triple(-1f, 1f, 1f)
        )

        // Define the 12 edges connecting the vertices
        val edges = listOf(
            0 to 1, 1 to 2, 2 to 3, 3 to 0, // Back face
            4 to 5, 5 to 6, 6 to 7, 7 to 4, // Front face
            0 to 4, 1 to 5, 2 to 6, 3 to 7  // Connecting edges
        )

        // Draw outer cube
        edges.forEach { (startIdx, endIdx) ->
            val v1 = vertices[startIdx]
            val v2 = vertices[endIdx]

            val p1 = project(v1.first, v1.second, v1.third, s1)
            val p2 = project(v2.first, v2.second, v2.third, s1)

            drawLine(
                color = Color.Cyan.copy(alpha = 0.6f),
                start = p1,
                end = p2,
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }

        // Draw inner cube
        edges.forEach { (startIdx, endIdx) ->
            val v1 = vertices[startIdx]
            val v2 = vertices[endIdx]

            val p1 = project(v1.first, v1.second, v1.third, s2)
            val p2 = project(v2.first, v2.second, v2.third, s2)

            drawLine(
                color = Color.Magenta.copy(alpha = 0.8f),
                start = p1,
                end = p2,
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )
        }

        // Draw connections between inner and outer cubes (Tesseract effect)
        for (i in 0 until 8) {
            val v = vertices[i]
            val pOuter = project(v.first, v.second, v.third, s1)
            val pInner = project(v.first, v.second, v.third, s2)

            drawLine(
                color = Color.Yellow.copy(alpha = 0.4f),
                start = pOuter,
                end = pInner,
                strokeWidth = 1.5f,
                cap = StrokeCap.Round
            )
        }
    }
}
