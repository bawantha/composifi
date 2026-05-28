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
import kotlinx.coroutines.isActive
import kotlin.random.Random

data class HyperSpaceStar(
    var x: Float,
    var y: Float,
    var z: Float,
    var pz: Float
)

@Composable
fun HyperSpaceJumpAnimation(modifier: Modifier = Modifier) {
    var tick by remember { mutableFloatStateOf(0f) }

    val stars = remember {
        List(400) {
            HyperSpaceStar(
                x = Random.nextFloat() * 2000f - 1000f,
                y = Random.nextFloat() * 2000f - 1000f,
                z = Random.nextFloat() * 2000f,
                pz = Random.nextFloat() * 2000f
            )
        }
    }

    LaunchedEffect(Unit) {
        val speed = 20f
        while (isActive) {
            withFrameNanos {
                stars.forEach { star ->
                    star.pz = star.z
                    star.z -= speed

                    if (star.z < 1) {
                        star.z = 2000f
                        star.x = Random.nextFloat() * 2000f - 1000f
                        star.y = Random.nextFloat() * 2000f - 1000f
                        star.pz = star.z
                    }
                }
                tick += 1f // Trigger recomposition
            }
        }
    }

    Canvas(modifier = modifier) {
        val _tick = tick // Read state to trigger redraw
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f

        stars.forEach { star ->
            val sx = map(star.x / star.z, 0f, 1f, 0f, width) + centerX
            val sy = map(star.y / star.z, 0f, 1f, 0f, height) + centerY

            val r = map(star.z, 0f, 2000f, 8f, 0f)

            drawCircle(
                color = Color.White,
                radius = r,
                center = Offset(sx, sy)
            )

            val px = map(star.x / star.pz, 0f, 1f, 0f, width) + centerX
            val py = map(star.y / star.pz, 0f, 1f, 0f, height) + centerY

            drawLine(
                color = Color.White.copy(alpha = 0.5f),
                start = Offset(px, py),
                end = Offset(sx, sy),
                strokeWidth = r / 2
            )
        }
    }
}

private fun map(value: Float, start1: Float, stop1: Float, start2: Float, stop2: Float): Float {
    return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1))
}
