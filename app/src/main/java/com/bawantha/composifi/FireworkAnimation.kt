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
import kotlin.random.Random

@Composable
fun FireworkAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val numParticles = 100
    val particles = remember {
        List(numParticles) {
            val angle = Random.nextFloat() * 2 * PI.toFloat()
            val speed = Random.nextFloat() * 100f + 50f
            val color = Color(
                red = 0.5f + Random.nextFloat() * 0.5f,
                green = 0.2f + Random.nextFloat() * 0.8f,
                blue = 0.5f + Random.nextFloat() * 0.5f,
                alpha = 1f
            )
            FireworkParticle(angle, speed, color)
        }
    }

    Canvas(modifier = modifier) {
        val cycleTime = 2f
        val localTime = time % cycleTime
        val progress = localTime / cycleTime

        val alpha = 1f - progress

        if (progress > 0.05f) { // slightly delay start
            val center = Offset(size.width / 2f, size.height / 2f)

            particles.forEach { particle ->
                val distance = particle.speed * localTime * (size.minDimension / 150f)
                val dx = cos(particle.angle) * distance
                // Add a gravity effect
                val dy = sin(particle.angle) * distance + (80f * localTime * localTime * (size.minDimension / 150f))

                val position = center + Offset(dx, dy)

                drawCircle(
                    color = particle.color.copy(alpha = alpha.coerceIn(0f, 1f)),
                    radius = size.minDimension * 0.02f * (1f - progress * 0.5f),
                    center = position
                )
            }
        }
    }
}

private data class FireworkParticle(
    val angle: Float,
    val speed: Float,
    val color: Color
)