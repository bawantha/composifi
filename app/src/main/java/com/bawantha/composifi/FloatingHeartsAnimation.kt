package com.bawantha.composifi

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

data class HeartParticle(
    val xOffset: Float,
    val speed: Float,
    val size: Float,
    val color: Color,
    val frequency: Float,
    val amplitude: Float,
    var progress: Float = 0f
)

@Composable
fun FloatingHeartsAnimation(modifier: Modifier = Modifier) {
    // Use standard mutableListOf instead of mutableStateListOf to avoid
    // modifying state inside the Canvas read-only snapshot.
    val particles = remember { mutableListOf<HeartParticle>() }

    LaunchedEffect(Unit) {
        val colors = listOf(
            Color(0xFFE91E63),
            Color(0xFFF44336),
            Color(0xFF9C27B0),
            Color(0xFFFF9800),
            Color(0xFF03A9F4)
        )

        while (true) {
            // Add a new particle
            if (particles.size < 20) {
                particles.add(
                    HeartParticle(
                        xOffset = Random.nextFloat(),
                        speed = Random.nextFloat() * 0.005f + 0.002f,
                        size = Random.nextFloat() * 40f + 20f,
                        color = colors.random(),
                        frequency = Random.nextFloat() * 3f + 1f,
                        amplitude = Random.nextFloat() * 50f + 20f,
                        progress = 1.2f // start from bottom
                    )
                )
            }
            delay(300)
        }
    }

    val infiniteTransition = rememberInfiniteTransition()
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(100000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        // use time so that canvas recomposes/redraws on each frame!
        val currentTime = time

        val iterator = particles.iterator()
        while (iterator.hasNext()) {
            val particle = iterator.next()
            particle.progress -= particle.speed

            if (particle.progress < -0.2f) {
                iterator.remove()
                continue
            }

            val x = size.width * particle.xOffset + sin(particle.progress * particle.frequency * PI.toFloat()) * particle.amplitude
            val y = size.height * particle.progress

            translate(left = x, top = y) {
                drawHeart(particle.size, particle.color)
            }
        }
    }
}

fun DrawScope.drawHeart(size: Float, color: Color) {
    val path = Path().apply {
        // Simple heart path shape
        val width = size
        val height = size
        moveTo(width / 2, height / 5)
        cubicTo(width / 8, -height / 4, -width / 4, height / 2, width / 2, height)
        cubicTo(width * 1.25f, height / 2, width * 0.875f, -height / 4, width / 2, height / 5)
        close()
    }
    drawPath(path, color)
}
