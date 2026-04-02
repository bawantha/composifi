package com.bawantha.composifi

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HeartbeatAnimation(
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
    baseSize: Float = 60f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "heartbeatTransition")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heartbeatScale"
    )

    Box(
        modifier = modifier.size((baseSize * 1.3f).dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = "Heart",
            tint = color,
            modifier = Modifier
                .size(baseSize.dp)
                .scale(scale)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeartbeatAnimationPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        HeartbeatAnimation(
            color = Color.Red,
            baseSize = 100f
        )
    }
}
