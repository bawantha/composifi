package com.bawantha.composifi

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.isActive
import kotlin.random.Random

data class HeartParticle(
    val id: Int,
    val startX: Float,
    val size: Float,
    val alpha: Float,
    val speed: Float, // speed in ratio per millisecond
    var currentY: Float = 1f // represents a ratio from 1f to 0f
)

@Composable
fun FloatingHeartsAnimation(modifier: Modifier = Modifier) {
    var particles by remember { mutableStateOf(listOf<HeartParticle>()) }

    // Generator effect loop
    LaunchedEffect(Unit) {
        var lastFrameTime = withFrameMillis { it }

        while (isActive) {
            withFrameMillis { frameTime ->
                val deltaMillis = frameTime - lastFrameTime
                lastFrameTime = frameTime

                val newParticles = mutableListOf<HeartParticle>()

                // move existing particles based on delta time
                particles.forEach { p ->
                    val newY = p.currentY - (p.speed * deltaMillis)
                    if (newY > -0.2f) { // Keep if still slightly visible
                        newParticles.add(p.copy(currentY = newY))
                    }
                }

                // Add new particles randomly (approx 2 particles per second = ~0.002 probability per ms)
                val spawnProbability = 0.002f * deltaMillis
                if (Random.nextFloat() < spawnProbability) {
                    newParticles.add(
                        HeartParticle(
                            id = Random.nextInt(),
                            startX = Random.nextFloat(), // 0f to 1f
                            size = Random.nextFloat() * 20f + 10f, // 10 to 30
                            alpha = Random.nextFloat() * 0.5f + 0.3f, // 0.3 to 0.8
                            speed = Random.nextFloat() * 0.0003f + 0.0001f, // per ms
                            currentY = 1.1f
                        )
                    )
                }
                particles = newParticles
            }
        }
    }

    // Pre-calculate the path for a 1x1 size to be scaled later
    val heartPath = remember {
        Path().apply {
            val width = 1f
            val height = 1f

            moveTo(width / 2, height / 5)
            cubicTo(
                width / 8, -height / 4,
                -width / 2, height / 3,
                width / 2, height
            )
            moveTo(width / 2, height / 5)
            cubicTo(
                width - width / 8, -height / 4,
                width + width / 2, height / 3,
                width / 2, height
            )
        }
    }

    Box(modifier = modifier.size(200.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            particles.forEach { particle ->
                val x = particle.startX * size.width
                val y = particle.currentY * size.height

                withTransform({
                    translate(left = x, top = y)
                    scale(scaleX = particle.size, scaleY = particle.size, pivot = androidx.compose.ui.geometry.Offset.Zero)
                }) {
                    drawPath(
                        path = heartPath,
                        color = Color.Red.copy(alpha = particle.alpha)
                    )
                }
            }
        }
    }
}
