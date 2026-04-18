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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class BlackHoleParticle(
    var angle: Float,
    var radius: Float,
    val speed: Float,
    val color: Color
)

@Composable
fun BlackHoleAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    val particles = remember {
        val colors = listOf(
            Color(0xFFFF9800), // Orange
            Color(0xFFFF5722), // Deep Orange
            Color(0xFFFFC107), // Amber
            Color(0xFF9C27B0), // Purple
            Color(0xFF3F51B5), // Indigo
            Color.White
        )
        List(200) {
            BlackHoleParticle(
                angle = Random.nextFloat() * 2f * PI.toFloat(),
                radius = Random.nextFloat() * 0.8f + 0.2f, // 0.2 to 1.0 of max radius
                speed = Random.nextFloat() * 0.5f + 0.5f,
                color = colors.random()
            )
        }
    }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val path = remember { Path() }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f
        val maxRadius = size.minDimension / 2f

        // Draw the event horizon (the black hole itself)
        drawCircle(
            color = Color.Black,
            radius = maxRadius * 0.2f,
            center = Offset(centerX, centerY)
        )

        // Draw the accretion disk glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF000000).copy(alpha = 0f),
                    Color(0xFFFF5722).copy(alpha = 0.5f),
                    Color(0xFF3F51B5).copy(alpha = 0f)
                ),
                center = Offset(centerX, centerY),
                radius = maxRadius * 0.8f
            ),
            radius = maxRadius * 0.8f,
            center = Offset(centerX, centerY)
        )

        // Draw spiraling particles
        for (particle in particles) {
            // Update particle position (this is safe as we are just calculating locally for drawing)
            // Actually, we should not modify the state object `particles` directly in the Canvas.
            // Since `time` is the only animated state we read, we calculate the current position based on `time`.

            // Particles spiral inwards and speed up as they get closer
            val currentRadiusOffset = (time * particle.speed * 0.1f) % particle.radius
            val currentRadius = maxRadius * (particle.radius - currentRadiusOffset)

            // If they get sucked in, they loop back to the outer edge
            val effectiveRadius = if (currentRadius < maxRadius * 0.2f) {
                 maxRadius * (particle.radius + (1f - particle.radius)) // reset to outer
            } else {
                 currentRadius
            }

            val currentAngle = particle.angle + time * particle.speed * (maxRadius / effectiveRadius) // Faster rotation closer to center

            val x = centerX + effectiveRadius * cos(currentAngle)
            val y = centerY + effectiveRadius * sin(currentAngle) * 0.5f // Squished to give perspective

            drawCircle(
                color = particle.color,
                radius = 2f * (effectiveRadius / maxRadius), // Particles look smaller further away
                center = Offset(x, y),
                alpha = 0.8f * (effectiveRadius / maxRadius) // Fade out near the center or edges
            )
        }
    }
}
