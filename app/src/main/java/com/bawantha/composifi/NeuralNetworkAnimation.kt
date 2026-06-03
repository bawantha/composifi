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
import kotlin.math.hypot
import kotlin.math.sin
import kotlin.random.Random

data class NeuralNode(
    val speedX: Float,
    val speedY: Float,
    val phaseX: Float,
    val phaseY: Float,
    val radius: Float
)

@Composable
fun NeuralNetworkAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val nodes = remember {
        List(25) {
            NeuralNode(
                speedX = Random.nextFloat() * 0.8f + 0.2f,
                speedY = Random.nextFloat() * 0.8f + 0.2f,
                phaseX = Random.nextFloat() * 2f * PI.toFloat(),
                phaseY = Random.nextFloat() * 2f * PI.toFloat(),
                radius = Random.nextFloat() * 3f + 1.5f
            )
        }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val maxDistance = size.minDimension * 0.4f

        // Read time to invalidate
        val currentTime = time

        val currentPositions = nodes.map { node ->
            val nx = width * 0.5f + (width * 0.5f - node.radius) * sin(currentTime * node.speedX + node.phaseX)
            val ny = height * 0.5f + (height * 0.5f - node.radius) * cos(currentTime * node.speedY + node.phaseY)
            Offset(nx, ny)
        }

        for (i in currentPositions.indices) {
            val p1 = currentPositions[i]

            drawCircle(
                color = Color(0xFF00F0FF),
                radius = nodes[i].radius,
                center = p1
            )

            for (j in i + 1 until currentPositions.size) {
                val p2 = currentPositions[j]
                val distance = hypot(p2.x - p1.x, p2.y - p1.y)

                if (distance < maxDistance) {
                    val alpha = (1f - (distance / maxDistance)).coerceIn(0f, 1f)
                    drawLine(
                        color = Color(0xFF00F0FF).copy(alpha = alpha),
                        start = p1,
                        end = p2,
                        strokeWidth = 2f,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}
