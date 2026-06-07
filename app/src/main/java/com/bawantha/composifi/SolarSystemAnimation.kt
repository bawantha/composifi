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
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SolarSystemAnimation(modifier: Modifier = Modifier) {
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
        val maxRadius = size.minDimension / 2f

        // Sun
        drawCircle(
            color = Color(0xFFFFD700),
            radius = maxRadius * 0.15f,
            center = Offset(centerX, centerY)
        )

        // Planets: distance from sun, speed, color
        val planets = listOf(
            Triple(0.3f, 2f, Color(0xFFAAAAAA)),   // Mercury
            Triple(0.5f, 1.5f, Color(0xFFE5AA70)), // Venus
            Triple(0.7f, 1f, Color(0xFF2E86C1)),   // Earth
            Triple(0.9f, 0.8f, Color(0xFFE74C3C))  // Mars
        )

        planets.forEach { (dist, speed, color) ->
            val orbitRadius = maxRadius * dist

            // Draw orbit
            drawCircle(
                color = Color.Gray.copy(alpha = 0.3f),
                radius = orbitRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 2f)
            )

            // Draw planet
            val angle = time * speed
            val planetX = centerX + orbitRadius * cos(angle)
            val planetY = centerY + orbitRadius * sin(angle)

            drawCircle(
                color = color,
                radius = maxRadius * 0.05f,
                center = Offset(planetX, planetY)
            )
        }
    }
}
