package com.bawantha.composifi

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun GlowingOrbAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "orbAnim")

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val maxRadius = size.minDimension / 2f

        // Outer glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF00FFFF).copy(alpha = 0.5f * pulse),
                    Color(0xFF0000FF).copy(alpha = 0.2f * pulse),
                    Color.Transparent
                ),
                center = Offset(centerX, centerY),
                radius = maxRadius
            ),
            radius = maxRadius,
            center = Offset(centerX, centerY)
        )

        // Inner core
        drawCircle(
            color = Color(0xFFE0FFFF),
            radius = maxRadius * 0.3f + (maxRadius * 0.1f * pulse),
            center = Offset(centerX, centerY)
        )
    }
}
