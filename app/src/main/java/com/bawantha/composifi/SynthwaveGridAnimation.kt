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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.isActive

@Composable
fun SynthwaveGridAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val gridColor = Color(0xFFE91E63) // Neon pink
    val sunTopColor = Color(0xFFFFEB3B) // Yellow
    val sunBottomColor = Color(0xFFF44336) // Red

    val gridPath = remember { Path() }

    Canvas(
        modifier = modifier.graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    ) {
        val width = size.width
        val height = size.height
        val horizonY = height * 0.45f

        // Draw Sun
        val sunRadius = size.minDimension * 0.35f
        val sunCenter = Offset(width / 2f, horizonY - sunRadius * 0.1f)

        drawCircle(
            brush = Brush.verticalGradient(
                colors = listOf(sunTopColor, sunBottomColor),
                startY = sunCenter.y - sunRadius,
                endY = sunCenter.y + sunRadius
            ),
            radius = sunRadius,
            center = sunCenter
        )

        // Cut out stripes from the bottom of the sun
        val stripeCount = 6
        for (i in 0 until stripeCount) {
            val progress = i / stripeCount.toFloat()
            val stripeThickness = sunRadius * 0.04f + progress * sunRadius * 0.06f
            val stripeY = sunCenter.y + progress * sunRadius

            drawLine(
                color = Color.Transparent,
                start = Offset(sunCenter.x - sunRadius, stripeY),
                end = Offset(sunCenter.x + sunRadius, stripeY),
                strokeWidth = stripeThickness,
                blendMode = BlendMode.Clear
            )
        }

        // Draw Grid
        gridPath.reset()

        // Vertical lines from horizon
        val numVerticalLines = 20
        for (i in -numVerticalLines..numVerticalLines) {
            val startX = width / 2f + (i * width * 0.08f)
            val endX = width / 2f + (i * width * 0.3f)

            gridPath.moveTo(startX, horizonY)
            gridPath.lineTo(endX, height)
        }

        // Horizontal lines moving forward
        val numHorizontalLines = 12
        val speed = 1.5f
        val offset = (time * speed) % 1f

        for (i in 0..numHorizontalLines) {
            val progress = (i + offset) / numHorizontalLines
            // Non-linear progress for perspective
            val perspectiveProgress = progress * progress * progress

            if (perspectiveProgress in 0f..1f) {
                val lineY = horizonY + (height - horizonY) * perspectiveProgress
                gridPath.moveTo(0f, lineY)
                gridPath.lineTo(width, lineY)
            }
        }

        drawPath(
            path = gridPath,
            color = gridColor,
            style = Stroke(width = 2f),
            alpha = 0.8f
        )

        // Draw Horizon line to cover up edges
        drawLine(
            color = gridColor,
            start = Offset(0f, horizonY),
            end = Offset(width, horizonY),
            strokeWidth = 3f
        )
    }
}
