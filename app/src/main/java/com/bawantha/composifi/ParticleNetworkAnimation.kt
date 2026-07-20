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

private data class NetworkParticle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val radius: Float,
    val color: Color
)

@Composable
fun ParticleNetworkAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val numParticles = 40
    val particles = remember {
        List(numParticles) {
            NetworkParticle(
                x = Random.nextFloat(), // relative 0 to 1
                y = Random.nextFloat(), // relative 0 to 1
                vx = (Random.nextFloat() - 0.5f) * 0.2f, // slow velocity
                vy = (Random.nextFloat() - 0.5f) * 0.2f,
                radius = Random.nextFloat() * 2f + 1f,
                color = Color(
                    red = 0.5f + Random.nextFloat() * 0.5f,
                    green = 0.5f + Random.nextFloat() * 0.5f,
                    blue = 0.5f + Random.nextFloat() * 0.5f,
                    alpha = 1f
                )
            )
        }
    }

    // Force read of time to invalidate canvas
    val currentTime = time

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // The time variable invalidates the frame
        // Update particles
        particles.forEach { p ->
            p.x += p.vx * 0.016f // approximate delta
            p.y += p.vy * 0.016f

            // Bounce off edges
            if (p.x < 0) { p.x = 0f; p.vx *= -1 }
            if (p.x > 1) { p.x = 1f; p.vx *= -1 }
            if (p.y < 0) { p.y = 0f; p.vy *= -1 }
            if (p.y > 1) { p.y = 1f; p.vy *= -1 }
        }

        val maxDistance = size.minDimension * 0.3f

        // Draw connections
        for (i in 0 until numParticles) {
            val p1 = particles[i]
            val x1 = p1.x * w
            val y1 = p1.y * h

            for (j in i + 1 until numParticles) {
                val p2 = particles[j]
                val x2 = p2.x * w
                val y2 = p2.y * h

                val dx = x1 - x2
                val dy = y1 - y2
                val dist = kotlin.math.sqrt(dx * dx + dy * dy)

                if (dist < maxDistance) {
                    val alpha = 1f - (dist / maxDistance)
                    drawLine(
                        color = Color.White.copy(alpha = alpha * 0.5f),
                        start = Offset(x1, y1),
                        end = Offset(x2, y2),
                        strokeWidth = 1f + alpha,
                        cap = StrokeCap.Round
                    )
                }
            }
        }

        // Draw particles
        particles.forEach { p ->
            drawCircle(
                color = p.color,
                radius = p.radius,
                center = Offset(p.x * w, p.y * h)
            )
        }
    }
}
