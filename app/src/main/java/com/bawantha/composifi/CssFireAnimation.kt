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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.isActive
import kotlin.random.Random

class CssFireParticle {
    var x = 0f
    var y = 0f
    var speed = 0f
    var radius = 0f
    var life = 0f
    var maxLife = 0f
    var hue = 0f

    fun reset(width: Float, height: Float) {
        x = Random.nextFloat() * width
        y = height + Random.nextFloat() * height * 0.2f
        speed = Random.nextFloat() * 100f + 50f
        radius = Random.nextFloat() * 40f + 10f
        maxLife = Random.nextFloat() * 1.5f + 0.5f
        life = maxLife
        // Fire hue from yellow (60) to red (0)
        hue = Random.nextFloat() * 60f
    }
}

@Composable
fun CssFireAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }
    var prevTime by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                prevTime = time
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val particles = remember { List(150) { CssFireParticle() } }

    Canvas(modifier = modifier) {
        val dt = if (prevTime == 0f) 0f else time - prevTime
        if (dt > 0.1f || dt < 0f) return@Canvas // handle big jumps

        val w = size.width
        val h = size.height

        // initialize particles if needed
        if (particles[0].maxLife == 0f) {
            particles.forEach { it.reset(w, h) }
        }

        particles.forEach { p ->
            p.y -= p.speed * dt
            p.life -= dt

            // Wobble
            p.x += (Random.nextFloat() - 0.5f) * 40f * dt

            if (p.life <= 0f || p.y < -p.radius) {
                p.reset(w, h)
                // When resetting, reset at the bottom
                p.y = h + Random.nextFloat() * 20f
            }

            val lifeRatio = (p.life / p.maxLife).coerceIn(0f, 1f)
            val alpha = lifeRatio
            val radius = p.radius * lifeRatio

            // HSV to Color: hue is 0 to 60 (red to yellow), saturation 1, value 1
            val color = Color.hsv(p.hue, 1f, 1f).copy(alpha = alpha * 0.8f)

            drawCircle(
                color = color,
                radius = radius,
                center = Offset(p.x, p.y),
                blendMode = BlendMode.Screen
            )
        }
    }
}
