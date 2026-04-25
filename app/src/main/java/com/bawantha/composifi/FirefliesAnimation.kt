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
import androidx.compose.ui.graphics.drawscope.Fill
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class FireflyAnimationParticleData(
    val initialX: Float,
    val initialY: Float,
    val speedX: Float,
    val speedY: Float,
    val phaseX: Float,
    val phaseY: Float,
    val sizeBase: Float,
    val glowPhase: Float,
    val color: Color
)

@Composable
fun FirefliesAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val particleCount = 20
    val fireflies = remember {
        val colors = listOf(
            Color(0xFFE6EE9C), // Light yellow green
            Color(0xFFFFF59D), // Light yellow
            Color(0xFFFFCC80), // Light orange
            Color(0xFFC5E1A5)  // Light green
        )
        List(particleCount) {
            FireflyAnimationParticleData(
                initialX = Random.nextFloat(),
                initialY = Random.nextFloat(),
                speedX = Random.nextFloat() * 0.5f + 0.1f,
                speedY = Random.nextFloat() * 0.5f + 0.1f,
                phaseX = Random.nextFloat() * 2f * PI.toFloat(),
                phaseY = Random.nextFloat() * 2f * PI.toFloat(),
                sizeBase = Random.nextFloat() * 4f + 2f,
                glowPhase = Random.nextFloat() * 2f * PI.toFloat(),
                color = colors.random()
            )
        }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        for (firefly in fireflies) {
            // Calculate movement
            val currentX = width * (firefly.initialX + sin(time * firefly.speedX + firefly.phaseX) * 0.3f)
            val currentY = height * (firefly.initialY + cos(time * firefly.speedY + firefly.phaseY) * 0.3f)

            // Wrap around the screen to keep them visible
            val wrappedX = (currentX % width + width) % width
            val wrappedY = (currentY % height + height) % height

            // Calculate pulsing glow effect
            val pulse = (sin(time * 3f + firefly.glowPhase) + 1f) / 2f // 0 to 1
            val radius = firefly.sizeBase + pulse * firefly.sizeBase
            val alpha = 0.3f + pulse * 0.7f

            // Draw core
            drawCircle(
                color = firefly.color.copy(alpha = alpha),
                radius = radius * 0.5f,
                center = Offset(wrappedX, wrappedY),
                style = Fill
            )

            // Draw glow
            drawCircle(
                color = firefly.color.copy(alpha = alpha * 0.3f),
                radius = radius,
                center = Offset(wrappedX, wrappedY),
                style = Fill
            )

            // Draw outer glow
            drawCircle(
                color = firefly.color.copy(alpha = alpha * 0.1f),
                radius = radius * 2f,
                center = Offset(wrappedX, wrappedY),
                style = Fill
            )
        }
    }
}
