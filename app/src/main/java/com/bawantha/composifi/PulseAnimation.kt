package com.bawantha.composifi

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PulseAnimation(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF6BDFE0),
    pulseRadius: Float = 100f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulseTransition")

    // The scale of the pulse
    val scale by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = pulseRadius,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseScale"
    )

    // The alpha (opacity) of the pulse
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = modifier.size((pulseRadius * 2).dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = color,
                radius = scale,
                alpha = alpha
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PulseAnimationPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        PulseAnimation(
            color = Color(0xFFCB2790),
            pulseRadius = 150f
        )
    }
}
