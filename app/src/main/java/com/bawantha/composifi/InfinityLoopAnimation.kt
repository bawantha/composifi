package com.bawantha.composifi

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun InfinityLoopAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val path = remember { Path() }

    Canvas(modifier = modifier) {
        val currentTime = time
        val width = size.width
        val height = size.height
        val centerX = width / 2
        val centerY = height / 2
        val scale = minOf(width, height) / 3

        path.reset()
        for (i in 0..100) {
            val t = i * 2 * PI / 100
            val denominator = 1 + sin(t) * sin(t)
            val x = scale * cos(t) / denominator
            val y = scale * sin(t) * cos(t) / denominator

            val pX = centerX + x.toFloat()
            val pY = centerY + y.toFloat()

            if (i == 0) {
                path.moveTo(pX, pY)
            } else {
                path.lineTo(pX, pY)
            }
        }

        drawPath(
            path = path,
            color = Color.hsv((currentTime * 100) % 360f, 1f, 1f),
            style = Stroke(width = 8f)
        )

        val tDot = (currentTime * 2) % (2 * PI)
        val denominatorDot = 1 + sin(tDot) * sin(tDot)
        val dotX = scale * cos(tDot) / denominatorDot
        val dotY = scale * sin(tDot) * cos(tDot) / denominatorDot

        drawCircle(
            color = Color.White,
            radius = 12f,
            center = Offset(centerX + dotX.toFloat(), centerY + dotY.toFloat())
        )
    }
}
