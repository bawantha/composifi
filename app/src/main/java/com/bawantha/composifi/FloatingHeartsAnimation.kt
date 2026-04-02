package com.bawantha.composifi

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun FloatingHeartsAnimation(
    modifier: Modifier = Modifier,
    heartColors: List<Color> = listOf(
        Color(0xFFFF5252),
        Color(0xFFFF4081),
        Color(0xFFE040FB),
        Color(0xFFF48FB1)
    )
) {
    val infiniteTransition = rememberInfiniteTransition(label = "floatingHearts")

    val heartCount = 8

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "globalProgress"
    )

    val randoms = remember {
        List(heartCount) {
            object {
                val delay = Random.nextFloat()
                val xOffset = Random.nextFloat() * 120f - 60f
                val speed = Random.nextFloat() * 0.5f + 0.5f
                val size = Random.nextFloat() * 20f + 20f
                val colorIndex = Random.nextInt(heartColors.size)
                val wobbleFreq = Random.nextFloat() * 3f + 1f
                val wobbleAmplitude = Random.nextFloat() * 20f + 10f
            }
        }
    }

    Box(
        modifier = modifier.size(150.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            randoms.forEach { params ->
                val p = (progress * params.speed + params.delay) % 1f

                val alpha = when {
                    p < 0.1f -> p / 0.1f
                    p > 0.8f -> (1f - p) / 0.2f
                    else -> 1f
                }

                val y = height - (p * height * 1.5f)
                val x = width / 2f + params.xOffset + sin(p * params.wobbleFreq * Math.PI.toFloat() * 2).toFloat() * params.wobbleAmplitude

                val scale = if (p < 0.1f) {
                    0.5f + (p / 0.1f) * 0.5f
                } else {
                    1f
                }

                drawHeart(
                    color = heartColors[params.colorIndex],
                    center = Offset(x, y),
                    size = params.size * scale,
                    alpha = alpha
                )
            }
        }
    }
}

private fun DrawScope.drawHeart(
    color: Color,
    center: Offset,
    size: Float,
    alpha: Float
) {
    val path = Path().apply {
        moveTo(center.x, center.y + size / 2)
        cubicTo(
            center.x - size, center.y - size / 4,
            center.x - size / 2, center.y - size,
            center.x, center.y - size / 4
        )
        cubicTo(
            center.x + size / 2, center.y - size,
            center.x + size, center.y - size / 4,
            center.x, center.y + size / 2
        )
        close()
    }

    drawPath(
        path = path,
        color = color,
        alpha = alpha
    )
}

@Preview(showBackground = true)
@Composable
fun FloatingHeartsAnimationPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        FloatingHeartsAnimation()
    }
}