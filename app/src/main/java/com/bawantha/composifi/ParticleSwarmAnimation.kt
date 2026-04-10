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
import kotlin.math.hypot
import kotlin.random.Random

@Composable
fun ParticleSwarmAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    // We generate some random particles
    val particles = remember {
        List(30) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                vx = (Random.nextFloat() - 0.5f) * 0.4f, // Velocity fraction per second
                vy = (Random.nextFloat() - 0.5f) * 0.4f
            )
        }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val maxDistance = size.minDimension * 0.4f

        // Calculate current positions based on time
        val currentPositions = particles.map { p ->
            var cx = (p.x + p.vx * time) % 2f
            if (cx < 0) cx += 2f
            var cy = (p.y + p.vy * time) % 2f
            if (cy < 0) cy += 2f

            val finalX = if (cx > 1f) 2f - cx else cx
            val finalY = if (cy > 1f) 2f - cy else cy

            Offset(finalX * width, finalY * height)
        }

        // Draw connections
        for (i in currentPositions.indices) {
            for (j in i + 1 until currentPositions.size) {
                val p1 = currentPositions[i]
                val p2 = currentPositions[j]
                val distance = hypot(p1.x - p2.x, p1.y - p2.y)

                if (distance < maxDistance) {
                    val alpha = 1f - (distance / maxDistance)
                    drawLine(
                        color = Color(0xFF00F0FF).copy(alpha = alpha),
                        start = p1,
                        end = p2,
                        strokeWidth = 2f
                    )
                }
            }
        }

        // Draw particles
        for (pos in currentPositions) {
            drawCircle(
                color = Color(0xFFFF007F),
                radius = 6f,
                center = pos
            )
        }
    }
}

private data class Particle(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float
)
