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
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.PI

@Composable
fun PhyllotaxisAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    Canvas(modifier = modifier) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val nParticles = 300
        val c = 8f // Scaling factor

        for (i in 0 until nParticles) {
            // angle = n * 137.5 degrees
            val a = i * 137.5f * (PI.toFloat() / 180f)
            // radius = c * sqrt(n)
            val r = c * sqrt(i.toFloat())

            // Add dynamic animation to the radius to make it pulse out
            val dynamicRadius = r + (sin(time * 3f + i * 0.1f) * 10f)

            val x = centerX + dynamicRadius * cos(a)
            val y = centerY + dynamicRadius * sin(a)

            val hue = (time * 50f + i) % 360f

            drawCircle(
                color = Color.hsv(hue, 0.8f, 1f, 1f),
                radius = 4f + (sin(time * 2f + i * 0.05f) * 2f),
                center = Offset(x, y)
            )
        }
    }
}
