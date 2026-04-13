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
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

@Composable
fun ConstellationAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    // Number of nodes
    val numNodes = 15

    // Generate stable random offsets and speeds for each node
    val nodesData = remember {
        List(numNodes) {
            NodeData(
                offsetX = Math.random().toFloat() * 100f,
                offsetY = Math.random().toFloat() * 100f,
                speedX = (Math.random().toFloat() * 0.5f + 0.1f) * (if (Math.random() > 0.5) 1 else -1),
                speedY = (Math.random().toFloat() * 0.5f + 0.1f) * (if (Math.random() > 0.5) 1 else -1),
                phaseX = Math.random().toFloat() * Math.PI.toFloat() * 2,
                phaseY = Math.random().toFloat() * Math.PI.toFloat() * 2
            )
        }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Max distance to draw a line between nodes
        val maxDistance = size.minDimension * 0.4f
        val nodeRadius = size.minDimension * 0.02f

        val currentPositions = nodesData.map { data ->
            // Use sin/cos with time to make them float around smoothly within bounds
            val x = width / 2f + (width / 2.5f) * sin(time * data.speedX + data.phaseX)
            val y = height / 2f + (height / 2.5f) * cos(time * data.speedY + data.phaseY)
            Offset(x, y)
        }

        // Draw connections
        for (i in 0 until numNodes) {
            for (j in i + 1 until numNodes) {
                val p1 = currentPositions[i]
                val p2 = currentPositions[j]
                val distance = hypot(p2.x - p1.x, p2.y - p1.y)

                if (distance < maxDistance) {
                    // Alpha depends on distance (closer = more opaque)
                    val alpha = (1f - (distance / maxDistance)).coerceIn(0f, 1f)
                    drawLine(
                        color = Color(0xFF00F0FF).copy(alpha = alpha * 0.8f),
                        start = p1,
                        end = p2,
                        strokeWidth = 2f
                    )
                }
            }
        }

        // Draw nodes
        for (pos in currentPositions) {
            drawCircle(
                color = Color.White,
                radius = nodeRadius,
                center = pos
            )
            // Add a slight glow to nodes
            drawCircle(
                color = Color(0xFF00F0FF).copy(alpha = 0.5f),
                radius = nodeRadius * 2.5f,
                center = pos
            )
        }
    }
}

private data class NodeData(
    val offsetX: Float,
    val offsetY: Float,
    val speedX: Float,
    val speedY: Float,
    val phaseX: Float,
    val phaseY: Float
)
