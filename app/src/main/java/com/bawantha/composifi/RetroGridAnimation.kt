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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import kotlinx.coroutines.isActive
import kotlin.math.pow

@Composable
fun RetroGridAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val gridPath = remember { Path() }
    val sunPath = remember { Path() }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val horizonY = height * 0.5f

        // Draw background gradient (dark purple to black)
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF0F0026), Color(0xFF000000)),
                startY = 0f,
                endY = height
            ),
            size = size
        )

        // Draw the Synthwave Sun
        val sunRadius = size.minDimension * 0.25f
        val sunCenter = Offset(width / 2f, horizonY - sunRadius * 0.3f)

        clipRect(top = 0f, bottom = horizonY) {
            drawCircle(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFD700), Color(0xFFFF007F)), // Yellow to Pink
                    startY = sunCenter.y - sunRadius,
                    endY = sunCenter.y + sunRadius
                ),
                radius = sunRadius,
                center = sunCenter
            )

            // Draw sun stripes (chopped effect)
            val numStripes = 6
            for (i in 0 until numStripes) {
                val stripeHeight = sunRadius * 0.08f * (i + 1)
                val stripeY = horizonY - (i * sunRadius * 0.2f)
                drawRect(
                    color = Color(0xFF0F0026), // Background color to cut out
                    topLeft = Offset(sunCenter.x - sunRadius, stripeY),
                    size = Size(sunRadius * 2, stripeHeight)
                )
            }
        }

        // Draw glow on horizon
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFFF007F).copy(alpha = 0.5f), Color.Transparent),
                startY = horizonY - 10f,
                endY = horizonY + height * 0.2f
            ),
            topLeft = Offset(0f, horizonY),
            size = Size(width, height - horizonY)
        )


        // Draw Perspective Grid
        val gridLines = 15
        val speed = 2f
        val yOffset = (time * speed) % 1f

        clipRect(top = horizonY, bottom = height) {
            gridPath.reset()

            // Vertical converging lines
            for (i in -gridLines..gridLines) {
                val xStart = width / 2f + (width * 2f * (i.toFloat() / gridLines))
                gridPath.moveTo(width / 2f, horizonY)
                gridPath.lineTo(xStart, height)
            }

            drawPath(
                path = gridPath,
                color = Color(0xFF00F0FF), // Neon Cyan
                style = Stroke(width = 2f)
            )

            // Horizontal lines moving forward
            for (i in 0..gridLines) {
                val normalizedY = (i.toFloat() + yOffset) / gridLines
                // Exponential curve to create perspective illusion
                val perspectiveY = horizonY + (height - horizonY) * normalizedY.pow(3)

                val alpha = (normalizedY * 1.5f).coerceIn(0f, 1f)

                drawLine(
                    color = Color(0xFFFF007F).copy(alpha = alpha), // Neon Pink
                    start = Offset(0f, perspectiveY),
                    end = Offset(width, perspectiveY),
                    strokeWidth = 2f + normalizedY * 3f
                )
            }
        }
    }
}
