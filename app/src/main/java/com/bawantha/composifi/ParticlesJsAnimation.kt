package com.bawantha.composifi

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlinx.coroutines.isActive
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class ParticleJsNode(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val radius: Float
)

@Composable
fun ParticlesJsAnimation(modifier: Modifier = Modifier) {
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
    val maxDistance = 150f

    val particles = remember {
        List(numParticles) {
            ParticleJsNode(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                vx = (Random.nextFloat() - 0.5f) * 0.005f,
                vy = (Random.nextFloat() - 0.5f) * 0.005f,
                radius = Random.nextFloat() * 4f + 2f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Read time to trigger recompose/redraw
        val currentTime = time

        particles.forEach { p ->
            p.x += p.vx
            p.y += p.vy

            if (p.x < 0f) p.x = 1f
            if (p.x > 1f) p.x = 0f
            if (p.y < 0f) p.y = 1f
            if (p.y > 1f) p.y = 0f

            val px = p.x * width
            val py = p.y * height

            drawCircle(
                color = Color.White,
                radius = p.radius,
                center = Offset(px, py),
                alpha = 0.8f
            )
        }

        for (i in 0 until numParticles) {
            for (j in i + 1 until numParticles) {
                val p1 = particles[i]
                val p2 = particles[j]

                val px1 = p1.x * width
                val py1 = p1.y * height
                val px2 = p2.x * width
                val py2 = p2.y * height

                val dist = Offset(px1 - px2, py1 - py2).getDistance()

                if (dist < maxDistance) {
                    val alpha = 1f - (dist / maxDistance)
                    drawLine(
                        color = Color.White,
                        start = Offset(px1, py1),
                        end = Offset(px2, py2),
                        strokeWidth = 2f,
                        alpha = alpha * 0.5f
                    )
                }
            }
        }
    }
}
