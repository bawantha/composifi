package com.bawantha.composifi

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import kotlinx.coroutines.isActive
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GalaxySpiralAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f
        val maxRadius = size.minDimension / 2f

        val numArms = 5
        val starsPerArm = 40

        for (arm in 0 until numArms) {
            val armOffset = (Math.PI * 2 / numArms) * arm
            for (i in 0 until starsPerArm) {
                val t = i.toFloat() / starsPerArm
                val distance = t * maxRadius
                val angle = armOffset + t * Math.PI * 3 + time * 0.5f

                val x = centerX + distance * cos(angle).toFloat()
                val y = centerY + distance * sin(angle).toFloat()

                val starSize = (1.5f + sin(time * 3f + i)) * (t + 0.2f) * 2f

                val hue = (time * 50f + arm * (360f / numArms) + t * 100f) % 360f
                val color = Color.hsv(hue = hue, saturation = 0.8f, value = 1f)

                drawCircle(
                    color = color,
                    radius = starSize,
                    center = Offset(x, y),
                    style = Fill,
                    alpha = 0.8f
                )
            }
        }
    }
}
