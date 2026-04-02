package com.bawantha.composifi

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ShimmeringStarAnimation(
    modifier: Modifier = Modifier,
    starColor: Color = Color(0xFFFFD700),
    pulseRadius: Float = 100f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "starTransition")

    // The scale of the star
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "starScale"
    )

    // The rotation of the star
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "starRotation"
    )

    // Shimmer effect (gradient sweep)
    // We'll calculate the actual translation based on Canvas size later
    val shimmerTranslateAnim by infiniteTransition.animateFloat(
        initialValue = -0.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "starShimmer"
    )

    val shimmerColors = listOf(
        starColor.copy(alpha = 0.6f),
        Color.White.copy(alpha = 0.9f),
        starColor.copy(alpha = 0.6f)
    )

    Box(
        modifier = modifier.size((pulseRadius * 2).dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Using size.minDimension / 2 allows radius to scale with device density
            // correctly since the Canvas matches the Box size.
            val baseRadius = (size.minDimension / 2) * 0.9f // small padding
            val center = Offset(size.width / 2, size.height / 2)
            val outerRadius = baseRadius * scale
            val innerRadius = outerRadius * 0.4f
            val numPoints = 5

            val path = Path()
            for (i in 0 until numPoints * 2) {
                val angle = (i * Math.PI / numPoints) + Math.toRadians(rotation.toDouble()) - Math.PI / 2
                val r = if (i % 2 == 0) outerRadius else innerRadius
                val x = center.x + r * cos(angle).toFloat()
                val y = center.y + r * sin(angle).toFloat()
                if (i == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }
            path.close()

            // Calculate translation pixel offset based on Canvas size
            val translateOffset = shimmerTranslateAnim * size.maxDimension

            // Draw the star with shimmer gradient
            val brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(translateOffset - size.maxDimension, translateOffset - size.maxDimension),
                end = Offset(translateOffset, translateOffset)
            )

            drawPath(
                path = path,
                brush = brush
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShimmeringStarAnimationPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ShimmeringStarAnimation(
            starColor = Color(0xFFFFD700),
            pulseRadius = 150f
        )
    }
}