package com.bawantha.composifi

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TusiCoupleAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = withInfiniteAnimationFrameMillis { it }
        while (isActive) {
            withInfiniteAnimationFrameMillis { frameTime ->
                time = (frameTime - startTime) / 1000f
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasSize = size.minDimension
        val center = Offset(size.width / 2, size.height / 2)
        val outerRadius = canvasSize / 2f * 0.9f
        val dotRadius = outerRadius * 0.08f
        val numDots = 8

        // Draw outer circle
        drawCircle(
            color = Color.DarkGray,
            radius = outerRadius,
            center = center,
            style = Stroke(width = 2f)
        )

        // Draw inner lines
        for (i in 0 until numDots) {
            val angle = i * PI / numDots
            val x1 = center.x + outerRadius * cos(angle).toFloat()
            val y1 = center.y + outerRadius * sin(angle).toFloat()
            val x2 = center.x - outerRadius * cos(angle).toFloat()
            val y2 = center.y - outerRadius * sin(angle).toFloat()
            drawLine(
                color = Color.LightGray.copy(alpha = 0.5f),
                start = Offset(x1, y1),
                end = Offset(x2, y2),
                strokeWidth = 1f
            )
        }

        // Draw dots
        for (i in 0 until numDots) {
            val phaseOffset = i * PI / numDots

            // The magic of Tusi Couple: linear motion based on cosine of time + phase
            // Speed of rotation: time * PI
            val linearDisplacement = cos(time * PI * 1.5 + phaseOffset).toFloat() * outerRadius

            // The angle determines the line the dot travels on
            val angle = i * PI / numDots

            val dotX = center.x + linearDisplacement * cos(angle).toFloat()
            val dotY = center.y + linearDisplacement * sin(angle).toFloat()

            // Color based on index
            val hue = (i * 360f / numDots)
            val dotColor = Color.hsv(hue, 0.8f, 1f)

            drawCircle(
                color = dotColor,
                radius = dotRadius,
                center = Offset(dotX, dotY)
            )
        }
    }
}
