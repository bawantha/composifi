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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.isActive

@Composable
fun CyberpunkGridAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val gridColor = Color(0xFF00F0FF) // Cyan
    val sunTopColor = Color(0xFFFFD700) // Yellow
    val sunBottomColor = Color(0xFFFF007F) // Pink

    val path = remember { Path() }

    Canvas(
        modifier = modifier.graphicsLayer {
            compositingStrategy = CompositingStrategy.Offscreen
        }
    ) {
        val width = size.width
        val height = size.height

        val horizonY = height * 0.55f

        // 1. Draw Sun (clipped above horizon)
        val sunRadius = size.minDimension * 0.35f
        val sunCenter = Offset(width / 2f, horizonY)

        val sunBrush = Brush.verticalGradient(
            colors = listOf(sunTopColor, sunBottomColor),
            startY = sunCenter.y - sunRadius,
            endY = sunCenter.y + sunRadius
        )

        clipRect(top = 0f, bottom = horizonY) {
            drawCircle(
                brush = sunBrush,
                radius = sunRadius,
                center = sunCenter
            )

            // Draw moving horizontal cutouts with BlendMode.Clear
            val numCutouts = 6
            val cutoutHeight = sunRadius * 0.12f

            for (i in 0 until numCutouts) {
                // Animate from bottom of the sun upwards
                val yOffsetBase = (sunRadius * 1.5f) / numCutouts
                val movingYOffset = (time * 25f + i * yOffsetBase) % (sunRadius * 1.5f)
                val yPos = sunCenter.y + sunRadius - movingYOffset

                if (yPos > sunCenter.y - sunRadius * 0.3f) {
                    // Thickness grows as it gets lower (perspective effect)
                    val progress = (yPos - (sunCenter.y - sunRadius * 0.3f)) / (sunRadius * 1.3f)
                    val thickness = cutoutHeight * progress

                    drawRect(
                        color = Color.Black, // Color is ignored by Clear mode
                        topLeft = Offset(sunCenter.x - sunRadius, yPos),
                        size = Size(sunRadius * 2, thickness),
                        blendMode = BlendMode.Clear
                    )
                }
            }
        }

        // 2. Draw 3D Perspective Grid (below horizon)
        val numVerticalLines = 15
        val numHorizontalLines = 12

        clipRect(top = horizonY, bottom = height) {
            path.reset()

            // Vertical perspective lines
            val vpX = width / 2f // Vanishing point X
            val vpY = horizonY // Vanishing point Y

            for (i in -numVerticalLines..numVerticalLines) {
                val startX = vpX
                val startY = vpY

                val spread = width * 1.5f
                val endX = vpX + (i.toFloat() / numVerticalLines) * spread
                val endY = height

                path.moveTo(startX, startY)
                path.lineTo(endX, endY)
            }

            // Horizontal perspective lines (scrolling forward)
            val fractionalOffset = (time * 3f) % 1f

            for (i in -1..numHorizontalLines) {
                val lineIndex = i + fractionalOffset

                if (lineIndex > 0 && lineIndex <= numHorizontalLines) {
                    val progress = lineIndex / numHorizontalLines.toFloat()
                    // Cubic easing for perspective
                    val perspectiveProgress = progress * progress * progress

                    val lineY = horizonY + perspectiveProgress * (height - horizonY)

                    if (lineY <= height) {
                        path.moveTo(0f, lineY)
                        path.lineTo(width, lineY)
                    }
                }
            }

            drawPath(
                path = path,
                color = gridColor,
                style = Stroke(width = 1.5f)
            )

            // Add a glow over the horizon line
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, gridColor, Color.Transparent),
                    startX = width * 0.1f,
                    endX = width * 0.9f
                ),
                start = Offset(0f, horizonY),
                end = Offset(width, horizonY),
                strokeWidth = 4f
            )
        }
    }
}
