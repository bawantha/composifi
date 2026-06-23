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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import kotlinx.coroutines.isActive
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TesseractAnimation(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val startTime = System.nanoTime()
        while (isActive) {
            withFrameNanos { frameTime ->
                time = (frameTime - startTime) / 1_000_000_000f
            }
        }
    }

    val vertices = remember {
        val points = mutableListOf<FloatArray>()
        for (i in 0 until 16) {
            val x = if (i and 1 != 0) 1f else -1f
            val y = if (i and 2 != 0) 1f else -1f
            val z = if (i and 4 != 0) 1f else -1f
            val w = if (i and 8 != 0) 1f else -1f
            points.add(floatArrayOf(x, y, z, w))
        }
        points
    }

    val edges = remember {
        val lines = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until 16) {
            for (j in 0 until 4) {
                val neighbor = i xor (1 shl j)
                if (neighbor > i) {
                    lines.add(i to neighbor)
                }
            }
        }
        lines
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f

        val projected2D = mutableListOf<Offset>()

        val angle = time * 0.5f

        val cosA = cos(angle)
        val sinA = sin(angle)

        for (v in vertices) {
            var x = v[0]
            var y = v[1]
            var z = v[2]
            var w = v[3]

            // Rotate XY
            var tx = x * cosA - y * sinA
            var ty = x * sinA + y * cosA
            x = tx
            y = ty

            // Rotate ZW
            var tz = z * cosA - w * sinA
            var tw = z * sinA + w * cosA
            z = tz
            w = tw

            // Rotate XW
            tx = x * cosA - w * sinA
            tw = x * sinA + w * cosA
            x = tx
            w = tw

            // 4D to 3D projection
            val distance4D = 2f
            val wFactor = 1f / (distance4D - w)
            val x3 = x * wFactor
            val y3 = y * wFactor
            val z3 = z * wFactor

            // 3D to 2D projection
            val distance3D = 2f
            val zFactor = 1f / (distance3D - z3)
            val x2 = x3 * zFactor
            val y2 = y3 * zFactor

            // Scale to screen
            val scale = size.minDimension * 0.4f
            projected2D.add(Offset(centerX + x2 * scale, centerY + y2 * scale))
        }

        // Draw edges
        for (edge in edges) {
            val p1 = projected2D[edge.first]
            val p2 = projected2D[edge.second]

            // Cool neon colors based on time and edge index
            val hue = (time * 50f + edge.first * 10f) % 360f
            val color = Color.hsv(hue, 1f, 1f)

            drawLine(
                color = color,
                start = p1,
                end = p2,
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }

        // Draw vertices
        for ((index, p) in projected2D.withIndex()) {
            val hue = (time * 50f + index * 20f) % 360f
            drawCircle(
                color = Color.hsv(hue, 1f, 1f),
                radius = 6f,
                center = p
            )
            drawCircle(
                color = Color.White,
                radius = 2f,
                center = p
            )
        }
    }
}
