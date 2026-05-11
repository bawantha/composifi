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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.isActive
import kotlin.math.sin

@Composable
fun KaleidoscopeAnimation(modifier: Modifier = Modifier) {
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
        val center = Offset(width / 2f, height / 2f)
        val maxRadius = size.minDimension / 2f

        val layers = 3
        val segments = 8

        val colors = listOf(
            Color(0xFFE91E63), // Pink
            Color(0xFF9C27B0), // Purple
            Color(0xFF3F51B5), // Indigo
            Color(0xFF00BCD4), // Cyan
            Color(0xFF4CAF50), // Green
            Color(0xFFFFEB3B), // Yellow
            Color(0xFFFF9800)  // Orange
        )

        for (layer in 0 until layers) {
            val layerRadius = maxRadius * (0.4f + 0.3f * (layer + 1))
            val layerRotation = time * 20f * if (layer % 2 == 0) 1 else -1
            val layerOffset = sin(time * 2f + layer) * 10f
            val baseColor = colors[(layer * 2 + time.toInt()) % colors.size]
            val nextColor = colors[(layer * 2 + time.toInt() + 1) % colors.size]

            val fraction = time - time.toInt()
            val color = Color(
                red = baseColor.red + (nextColor.red - baseColor.red) * fraction,
                green = baseColor.green + (nextColor.green - baseColor.green) * fraction,
                blue = baseColor.blue + (nextColor.blue - baseColor.blue) * fraction,
                alpha = 0.8f
            )

            rotate(layerRotation, center) {
                for (segment in 0 until segments) {
                    val angle = (segment * (360f / segments))
                    rotate(angle, center) {
                        path.reset()
                        val p1 = Offset(center.x, center.y - layerOffset)
                        val p2 = Offset(center.x - layerRadius * 0.3f, center.y - layerRadius)
                        val p3 = Offset(center.x + layerRadius * 0.3f, center.y - layerRadius)

                        path.moveTo(p1.x, p1.y)
                        path.lineTo(p2.x, p2.y)
                        path.lineTo(p3.x, p3.y)
                        path.close()

                        drawPath(
                            path = path,
                            color = color,
                            style = Stroke(
                                width = 3f,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )

                        drawPath(
                            path = path,
                            color = color.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
    }
}
