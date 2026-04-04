package com.bawantha.composifi

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.isActive

class ConfettiParticle(
    var x: Float,
    var y: Float,
    var speedY: Float,
    var speedX: Float,
    val color: Color,
    val particleSize: Float,
    var rotation: Float,
    var rotationSpeed: Float
) {
    fun update(dt: Float, width: Float, height: Float) {
        y += speedY * dt
        x += speedX * dt
        rotation += rotationSpeed * dt

        if (y > height + particleSize && height > 0) {
            y = -particleSize
            x = (Math.random() * width).toFloat()
        }

        if (x > width + particleSize && width > 0) {
            x = -particleSize
        } else if (x < -particleSize && width > 0) {
            x = width + particleSize
        }
    }

    fun draw(drawScope: DrawScope) {
        drawScope.rotate(rotation, Offset(x, y)) {
            val halfSize: Float = particleSize / 2f
            val left: Float = x - halfSize
            val top: Float = y - halfSize
            drawRect(
                color = color,
                topLeft = Offset(x = left, y = top),
                size = androidx.compose.ui.geometry.Size(width = particleSize, height = particleSize)
            )
        }
    }
}

@Composable
fun ConfettiAnimation(modifier: Modifier = Modifier) {
    val particles = remember { mutableListOf<ConfettiParticle>() }
    var timeTicks by remember { mutableLongStateOf(0L) }
    val sizeHolder = remember { FloatArray(2) { 1000f } }

    LaunchedEffect(Unit) {
        if (particles.isEmpty()) {
            val colors = listOf(
                Color(0xFFFFC107), Color(0xFFFF5722), Color(0xFF4CAF50),
                Color(0xFF03A9F4), Color(0xFF9C27B0), Color(0xFFE91E63)
            )
            for (i in 0 until 120) {
                particles.add(
                    ConfettiParticle(
                        x = (Math.random() * 1000f).toFloat(),
                        y = (Math.random() * 1000f).toFloat() - 1000f,
                        speedY = (Math.random() * 300 + 200).toFloat(),
                        speedX = (Math.random() * 100 - 50).toFloat(),
                        color = colors[(Math.random() * colors.size).toInt()],
                        particleSize = (Math.random() * 20 + 15).toFloat(),
                        rotation = (Math.random() * 360).toFloat(),
                        rotationSpeed = (Math.random() * 200 - 100).toFloat()
                    )
                )
            }
        }

        var lastFrameTime = withFrameNanos { it }
        while (isActive) {
            val currentFrameTime = withFrameNanos { it }
            val dt = (currentFrameTime - lastFrameTime) / 1_000_000_000f
            lastFrameTime = currentFrameTime

            val w = sizeHolder[0]
            val h = sizeHolder[1]

            for (i in particles.indices) {
                particles[i].update(dt, w, h)
            }

            timeTicks = currentFrameTime
        }
    }

    Canvas(modifier = modifier) {
        timeTicks.hashCode()

        sizeHolder[0] = size.width
        sizeHolder[1] = size.height

        for (i in particles.indices) {
            particles[i].draw(this)
        }
    }
}
