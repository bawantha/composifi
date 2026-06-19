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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun InternetCoolAnimationEffects(modifier: Modifier = Modifier) {
    var time by remember { mutableFloatStateOf(0f) }

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
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f

        // Scale appropriately for different screen sizes
        val scale = size.minDimension * 0.25f

        val points = 300
        val numCurves = 5

        for (i in 0 until numCurves) {
            // Generate a color using HSV, varying hue based on curve index and time
            val hue = (time * 50f + i * (360f / numCurves)) % 360f
            val color = Color.hsv(hue, 0.8f, 1f)

            // Phase offset based on curve index
            val phaseOffset = i * (PI * 2 / numCurves).toFloat()

            path.reset()
            for (j in 0..points) {
                // Parameter t for the parametric equation
                val t = (j.toFloat() / points) * 2f * PI.toFloat()

                // A complex parametric curve (e.g., related to Maurer roses or harmonographs)
                // We incorporate 'time' into the parameters to make it animate dynamically.
                val r = scale * (sin(3f * t + time) + cos(5f * t - time*1.5f))

                val x = centerX + r * cos(t + time*0.5f + phaseOffset)
                val y = centerY + r * sin(t + time*0.5f + phaseOffset)

                if (j == 0) {
                    path.moveTo(x.toFloat(), y.toFloat())
                } else {
                    path.lineTo(x.toFloat(), y.toFloat())
                }
            }

            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = 3f + (sin(time * 3f + phaseOffset).toFloat() + 1f) * 1.5f,
                    cap = StrokeCap.Round
                ),
                alpha = 0.9f
            )
        }
    }
}
