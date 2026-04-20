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
import kotlinx.coroutines.isActive
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SpinningCubeAnimation(modifier: Modifier = Modifier) {
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
        val minDimension = size.minDimension

        // Cube configuration
        val cubeSize = minDimension * 0.4f
        val focalLength = minDimension * 1.5f

        // Define the 8 vertices of a cube centered at origin
        val vertices = listOf(
            floatArrayOf(-cubeSize, -cubeSize, -cubeSize),
            floatArrayOf(cubeSize, -cubeSize, -cubeSize),
            floatArrayOf(cubeSize, cubeSize, -cubeSize),
            floatArrayOf(-cubeSize, cubeSize, -cubeSize),
            floatArrayOf(-cubeSize, -cubeSize, cubeSize),
            floatArrayOf(cubeSize, -cubeSize, cubeSize),
            floatArrayOf(cubeSize, cubeSize, cubeSize),
            floatArrayOf(-cubeSize, cubeSize, cubeSize)
        )

        // Define the 12 edges of the cube (connecting vertex indices)
        val edges = listOf(
            Pair(0, 1), Pair(1, 2), Pair(2, 3), Pair(3, 0), // Front face
            Pair(4, 5), Pair(5, 6), Pair(6, 7), Pair(7, 4), // Back face
            Pair(0, 4), Pair(1, 5), Pair(2, 6), Pair(3, 7)  // Connecting edges
        )

        // Rotation angles
        val angleX = time * 0.8f
        val angleY = time * 1.2f
        val angleZ = time * 0.5f

        // Projected 2D points
        val projectedPoints = Array(8) { Offset.Zero }

        for (i in vertices.indices) {
            val v = vertices[i]
            var x = v[0]
            var y = v[1]
            var z = v[2]

            // Rotate around X axis
            var tempY = y * cos(angleX) - z * sin(angleX)
            var tempZ = y * sin(angleX) + z * cos(angleX)
            y = tempY
            z = tempZ

            // Rotate around Y axis
            var tempX = x * cos(angleY) + z * sin(angleY)
            tempZ = -x * sin(angleY) + z * cos(angleY)
            x = tempX
            z = tempZ

            // Rotate around Z axis
            tempX = x * cos(angleZ) - y * sin(angleZ)
            tempY = x * sin(angleZ) + y * cos(angleZ)
            x = tempX
            y = tempY

            // Move cube away from camera
            z += focalLength * 0.8f

            // Perspective projection
            val perspective = focalLength / (focalLength + z)
            val projX = x * perspective + width / 2f
            val projY = y * perspective + height / 2f

            projectedPoints[i] = Offset(projX, projY)
        }

        // Colors for edges to make it look cool
        val colors = listOf(
            Color(0xFF00FFFF), // Cyan
            Color(0xFFFF00FF), // Magenta
            Color(0xFFFFFF00)  // Yellow
        )

        // Draw edges
        for (i in edges.indices) {
            val edge = edges[i]
            val color = colors[i % colors.size]
            val p1 = projectedPoints[edge.first]
            val p2 = projectedPoints[edge.second]

            // Calculate a fake depth based alpha
            val z1 = vertices[edge.first][2] // Original Z isn't quite right for depth shading without full transform, but good enough for simple effect
            // A better way is to use the transformed Z, but let's just use a constant alpha for simplicity or a simple pulsing effect
            val pulseAlpha = 0.5f + 0.5f * sin(time * 2f + i).toFloat()

            drawLine(
                color = color,
                start = p1,
                end = p2,
                strokeWidth = 4f + 2f * pulseAlpha,
                cap = StrokeCap.Round,
                alpha = 0.6f + 0.4f * pulseAlpha
            )
        }

        // Draw vertices
        for (i in projectedPoints.indices) {
            drawCircle(
                color = Color.White,
                radius = 6f,
                center = projectedPoints[i],
                alpha = 0.8f
            )
        }
    }
}
