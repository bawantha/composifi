package com.bawantha.composifi

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun MetaballAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "MetaballTransition")

    // We'll create a smooth oscillating animation for multiple elements using sine/cosine waves based on this animated fraction.
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000),
            repeatMode = RepeatMode.Restart
        ),
        label = "MetaballProgress"
    )

    val path = remember { Path() }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Define a base radius relative to the size of the canvas to be responsive.
        val baseRadius = min(width, height) * 0.15f

        // Center point of the canvas
        val centerX = width / 2f
        val centerY = height / 2f

        // Let's create four dynamic positions moving in Lissajous-like patterns.
        val p1 = Offset(
            centerX + cos(animationProgress) * (width * 0.2f),
            centerY + sin(animationProgress * 2) * (height * 0.2f)
        )

        val p2 = Offset(
            centerX + sin(animationProgress * 1.5f + PI.toFloat() / 2) * (width * 0.25f),
            centerY + cos(animationProgress * 0.5f) * (height * 0.25f)
        )

        val p3 = Offset(
            centerX + cos(animationProgress * 2.5f + PI.toFloat()) * (width * 0.15f),
            centerY + sin(animationProgress * 1.2f + PI.toFloat()) * (height * 0.15f)
        )

        val p4 = Offset(
            centerX + sin(animationProgress * 0.8f + PI.toFloat() * 1.5f) * (width * 0.3f),
            centerY + cos(animationProgress * 1.8f + PI.toFloat() * 1.5f) * (height * 0.3f)
        )


        val metaballColor = Color(0xFFE91E63) // Cool pink color

        // Draw standard circles first
        drawCircle(color = metaballColor, center = p1, radius = baseRadius)
        drawCircle(color = metaballColor, center = p2, radius = baseRadius * 0.8f)
        drawCircle(color = metaballColor, center = p3, radius = baseRadius * 1.2f)
        drawCircle(color = metaballColor, center = p4, radius = baseRadius * 0.9f)


        // Now, pair up circles to draw the connecting bridges (the "goo" effect).
        // To make it look like a unified liquid, we test all major pairs.
        drawMetaballBridge(p1, p2, baseRadius, baseRadius * 0.8f, metaballColor, path)
        drawMetaballBridge(p1, p3, baseRadius, baseRadius * 1.2f, metaballColor, path)
        drawMetaballBridge(p1, p4, baseRadius, baseRadius * 0.9f, metaballColor, path)
        drawMetaballBridge(p2, p3, baseRadius * 0.8f, baseRadius * 1.2f, metaballColor, path)
        drawMetaballBridge(p2, p4, baseRadius * 0.8f, baseRadius * 0.9f, metaballColor, path)
        drawMetaballBridge(p3, p4, baseRadius * 1.2f, baseRadius * 0.9f, metaballColor, path)
    }
}

// Function adapted to draw gooey bridges between two circles of potentially different radii.
fun DrawScope.drawMetaballBridge(
    center1: Offset,
    center2: Offset,
    radius1: Float,
    radius2: Float,
    color: Color,
    path: Path
) {
    val distance = center1.metaballDistanceTo(center2)
    val maxDist = radius1 + radius2 + maxOf(radius1, radius2) * 1.5f // Threshold for when they detach

    if (distance <= maxDist && distance > kotlin.math.abs(radius1 - radius2)) {
        val u1 = 1.0 + (distance / maxDist) * 1.5
        val u2 = 1.0 + (distance / maxDist) * 1.5

        // Simplify v calculation for stability
        val v1 = min(1.0, sqrt(maxDist / distance.toDouble()) * 0.5)
        val v2 = min(1.0, sqrt(maxDist / distance.toDouble()) * 0.5)

        val angleBetweenCenters = atan2((center2.y - center1.y).toDouble(), (center2.x - center1.x).toDouble())

        // Compute spread angles for each circle
        val spread1 = acos(((radius1 - radius1 * v1) / distance).coerceIn(-1.0, 1.0))
        val spread2 = acos(((radius2 - radius2 * v2) / distance).coerceIn(-1.0, 1.0))

        val angle1A = angleBetweenCenters + spread1
        val angle1B = angleBetweenCenters - spread1
        val angle2A = angleBetweenCenters + PI - spread2
        val angle2B = angleBetweenCenters + PI + spread2

        val p1a = Offset(
            (center1.x + radius1 * cos(angle1A)).toFloat(),
            (center1.y + radius1 * sin(angle1A)).toFloat()
        )
        val p1b = Offset(
            (center1.x + radius1 * cos(angle1B)).toFloat(),
            (center1.y + radius1 * sin(angle1B)).toFloat()
        )
        val p2a = Offset(
            (center2.x + radius2 * cos(angle2A)).toFloat(),
            (center2.y + radius2 * sin(angle2A)).toFloat()
        )
        val p2b = Offset(
            (center2.x + radius2 * cos(angle2B)).toFloat(),
            (center2.y + radius2 * sin(angle2B)).toFloat()
        )

        val controlPoint = Offset(
            (0.5 * (center1.x + center2.x)).toFloat(),
            (0.5 * (center1.y + center2.y)).toFloat()
        )

        path.reset()
        path.moveTo(p1a.x, p1a.y)
        path.cubicTo(
            ((p1a.x + u1 * controlPoint.x) / (1 + u1)).toFloat(),
            ((p1a.y + u1 * controlPoint.y) / (1 + u1)).toFloat(),
            ((p2a.x + u2 * controlPoint.x) / (1 + u2)).toFloat(),
            ((p2a.y + u2 * controlPoint.y) / (1 + u2)).toFloat(),
            p2a.x, p2a.y
        )
        path.lineTo(p2b.x, p2b.y)
        path.cubicTo(
            ((p2b.x + u2 * controlPoint.x) / (1 + u2)).toFloat(),
            ((p2b.y + u2 * controlPoint.y) / (1 + u2)).toFloat(),
            ((p1b.x + u1 * controlPoint.x) / (1 + u1)).toFloat(),
            ((p1b.y + u1 * controlPoint.y) / (1 + u1)).toFloat(),
            p1b.x, p1b.y
        )
        path.close()

        drawPath(path, color = color)
    }
}

fun Offset.metaballDistanceTo(other: Offset): Float {
    return sqrt((x - other.x).pow(2) + (y - other.y).pow(2))
}
