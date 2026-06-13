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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CyberRingAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val numRings = 4
    val particlesPerRing = 15

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f

        val baseRadius = size.minDimension * 0.4f

        for (ring in 0 until numRings) {
            val ringRadius = baseRadius * (1f - ring * 0.2f)
            val ringSpeed = (if (ring % 2 == 0) 1f else -1f) * (0.5f + ring * 0.2f)

            for (i in 0 until particlesPerRing) {
                val angle = (i.toFloat() / particlesPerRing) * 2f * PI.toFloat() + time * ringSpeed

                // Add some wobble
                val wobble = sin(time * 3f + i) * (5f + ring * 2f)
                val currentRadius = ringRadius + wobble

                val x = centerX + currentRadius * cos(angle)
                val y = centerY + currentRadius * sin(angle)

                val alpha = (sin(time * 2f + i.toFloat() * 0.5f) + 1f) / 2f * 0.8f + 0.2f

                val hue = (time * 40f + ring * 60f + i * 10f) % 360f
                val color = Color.hsv(hue, 0.8f, 1f, alpha.toFloat())

                drawCircle(
                    color = color,
                    radius = 3f + ring * 1.5f,
                    center = Offset(x, y)
                )

                // Connect to next particle in the same ring
                val nextAngle = ((i + 1).toFloat() / particlesPerRing) * 2f * PI.toFloat() + time * ringSpeed
                val nextWobble = sin(time * 3f + (i + 1)) * (5f + ring * 2f)
                val nextRadius = ringRadius + nextWobble
                val nextX = centerX + nextRadius * cos(nextAngle)
                val nextY = centerY + nextRadius * sin(nextAngle)

                drawLine(
                    color = color.copy(alpha = alpha.toFloat() * 0.5f),
                    start = Offset(x, y),
                    end = Offset(nextX, nextY),
                    strokeWidth = 2f,
                    cap = StrokeCap.Round
                )

                // Random cross connections between rings
                if (ring < numRings - 1 && i % 3 == 0) {
                    val innerRingRadius = baseRadius * (1f - (ring + 1) * 0.2f)
                    val innerRingSpeed = (if ((ring + 1) % 2 == 0) 1f else -1f) * (0.5f + (ring + 1) * 0.2f)
                    val innerAngle = (i.toFloat() / particlesPerRing) * 2f * PI.toFloat() + time * innerRingSpeed
                    val innerWobble = sin(time * 3f + i) * (5f + (ring + 1) * 2f)
                    val innerRadius = innerRingRadius + innerWobble

                    val innerX = centerX + innerRadius * cos(innerAngle)
                    val innerY = centerY + innerRadius * sin(innerAngle)

                    drawLine(
                        color = color.copy(alpha = alpha.toFloat() * 0.3f),
                        start = Offset(x, y),
                        end = Offset(innerX, innerY),
                        strokeWidth = 1f,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}
