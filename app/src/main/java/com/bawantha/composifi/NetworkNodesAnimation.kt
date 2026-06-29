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
import kotlin.random.Random

data class NetworkNodeParticle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val radius: Float,
    val color: Color
)

@Composable
fun NetworkNodesAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val colors = listOf(
        Color(0xFF00F0FF),
        Color(0xFFFF007F),
        Color(0xFF7FFF00)
    )

    val nodes = remember {
        List(20) {
            NetworkNodeParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                vx = (Random.nextFloat() - 0.5f) * 2f,
                vy = (Random.nextFloat() - 0.5f) * 2f,
                radius = 4f + Random.nextFloat() * 4f,
                color = colors.random()
            )
        }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Trigger recomposition/redraw
        val t = time

        // Update positions
        nodes.forEach { node ->
            node.x += node.vx * 0.01f
            node.y += node.vy * 0.01f

            if (node.x < 0f || node.x > 1f) node.vx *= -1f
            if (node.y < 0f || node.y > 1f) node.vy *= -1f
        }

        // Draw connections
        val maxDist = size.minDimension * 0.4f
        for (i in nodes.indices) {
            for (j in i + 1 until nodes.size) {
                val n1 = nodes[i]
                val n2 = nodes[j]
                val p1 = Offset(n1.x * width, n1.y * height)
                val p2 = Offset(n2.x * width, n2.y * height)
                val dist = (p1 - p2).getDistance()

                if (dist < maxDist) {
                    val alpha = 1f - (dist / maxDist)
                    drawLine(
                        color = Color.White.copy(alpha = alpha * 0.5f),
                        start = p1,
                        end = p2,
                        strokeWidth = 2f,
                        cap = StrokeCap.Round
                    )
                }
            }
        }

        // Draw nodes
        nodes.forEach { node ->
            drawCircle(
                color = node.color,
                radius = node.radius,
                center = Offset(node.x * width, node.y * height)
            )
        }
    }
}
