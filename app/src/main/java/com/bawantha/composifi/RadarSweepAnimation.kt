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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun RadarSweepAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val blips = remember {
        List(5) {
            val angle = Random.nextFloat() * 2 * PI.toFloat()
            val distance = Random.nextFloat() * 0.8f + 0.1f // 10% to 90% of radius
            Pair(angle, distance)
        }
    }

    Canvas(modifier = modifier) {
        val radius = size.minDimension / 2f
        val center = Offset(size.width / 2f, size.height / 2f)

        // Background color
        drawCircle(
            color = Color(0xFF0A2E0A), // Dark green background
            radius = radius,
            center = center
        )

        // Draw background circles
        for (i in 1..4) {
            drawCircle(
                color = Color(0xFF00FF00).copy(alpha = 0.4f),
                radius = radius * (i / 4f),
                center = center,
                style = Stroke(width = 2f)
            )
        }

        // Draw crosshairs
        drawLine(
            color = Color(0xFF00FF00).copy(alpha = 0.4f),
            start = Offset(center.x, center.y - radius),
            end = Offset(center.x, center.y + radius),
            strokeWidth = 2f
        )
        drawLine(
            color = Color(0xFF00FF00).copy(alpha = 0.4f),
            start = Offset(center.x - radius, center.y),
            end = Offset(center.x + radius, center.y),
            strokeWidth = 2f
        )

        // Rotation angle based on time (1 full rotation every 2 seconds)
        val sweepAngle = (time * 180f) % 360f // degrees

        rotate(degrees = sweepAngle, pivot = center) {
            drawArc(
                brush = Brush.sweepGradient(
                    0.0f to Color.Transparent,
                    0.75f to Color.Transparent,
                    1.0f to Color(0xFF00FF00).copy(alpha = 0.6f),
                    center = center
                ),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = true,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2)
            )
            // Draw leading line
            drawLine(
                color = Color(0xFF00FF00),
                start = center,
                end = Offset(center.x + radius, center.y),
                strokeWidth = 3f
            )
        }

        // Draw blips
        blips.forEach { (blipAngleRad, distancePct) ->
            val blipDistance = radius * distancePct
            val blipX = center.x + blipDistance * cos(blipAngleRad)
            val blipY = center.y + blipDistance * sin(blipAngleRad)

            // Calculate blip angle in degrees (0 to 360)
            var blipAngleDeg = Math.toDegrees(blipAngleRad.toDouble()).toFloat()
            if (blipAngleDeg < 0) blipAngleDeg += 360f

            // Calculate angular distance between sweep and blip
            var angleDiff = sweepAngle - blipAngleDeg
            if (angleDiff < 0) angleDiff += 360f

            // Alpha decays as the sweep moves away
            val blipAlpha = if (angleDiff < 180f) {
                1f - (angleDiff / 180f)
            } else {
                0f
            }

            if (blipAlpha > 0) {
                drawCircle(
                    color = Color(0xFF00FF00).copy(alpha = blipAlpha),
                    radius = 8f,
                    center = Offset(blipX, blipY)
                )
                drawCircle(
                    color = Color(0xFFFFFFFF).copy(alpha = blipAlpha),
                    radius = 3f,
                    center = Offset(blipX, blipY)
                )
            }
        }
    }
}
