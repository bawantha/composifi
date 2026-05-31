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

private data class TesseractPoint3D(var x: Float, var y: Float, var z: Float)
private data class TesseractPoint4D(var x: Float, var y: Float, var z: Float, var w: Float)

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

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val cx = width / 2f
        val cy = height / 2f
        val scale = size.minDimension * 0.4f
        val distance = 2f

        val points = mutableListOf<TesseractPoint4D>()
        for (x in listOf(-1f, 1f)) {
            for (y in listOf(-1f, 1f)) {
                for (z in listOf(-1f, 1f)) {
                    for (w in listOf(-1f, 1f)) {
                        points.add(TesseractPoint4D(x, y, z, w))
                    }
                }
            }
        }

        val projected3d = mutableListOf<TesseractPoint3D>()
        for (i in points.indices) {
            val p = points[i]

            // Rotation XW
            val angleXW = time * 0.5f
            var nw = p.w * cos(angleXW) - p.x * sin(angleXW)
            var nx = p.w * sin(angleXW) + p.x * cos(angleXW)
            var ny = p.y
            var nz = p.z

            // Rotation ZW
            val angleZW = time * 0.3f
            val nnw = nw * cos(angleZW) - nz * sin(angleZW)
            val nnz = nw * sin(angleZW) + nz * cos(angleZW)
            nw = nnw
            nz = nnz

            // 4D to 3D projection
            val wProj = 1f / (distance - nw)
            val px = nx * wProj
            val py = ny * wProj
            val pz = nz * wProj

            projected3d.add(TesseractPoint3D(px, py, pz))
        }

        val projected2d = mutableListOf<Offset>()
        for (i in projected3d.indices) {
            val p = projected3d[i]

            // Rotation YZ
            val angleYZ = time * 0.7f
            var ny = p.y * cos(angleYZ) - p.z * sin(angleYZ)
            var nz = p.y * sin(angleYZ) + p.z * cos(angleYZ)
            val nx = p.x

            // Rotation XY
            val angleXY = time * 0.4f
            val nnx = nx * cos(angleXY) - ny * sin(angleXY)
            val nny = nx * sin(angleXY) + ny * cos(angleXY)

            // 3D to 2D projection
            val zProj = 1f / (distance - nz)
            val px = nnx * zProj * scale + cx
            val py = nny * zProj * scale + cy

            projected2d.add(Offset(px, py))
        }

        fun drawConnection(i: Int, j: Int) {
            val hue = (time * 30f + i * 15f) % 360f
            drawLine(
                color = Color.hsv(hue, 1f, 1f),
                start = projected2d[i],
                end = projected2d[j],
                strokeWidth = 4f,
                cap = StrokeCap.Round,
                alpha = 0.8f
            )
        }

        // Draw connections
        for (i in 0 until 16) {
            if (i % 2 == 0) drawConnection(i, i + 1)
            if ((i / 2) % 2 == 0) drawConnection(i, i + 2)
            if ((i / 4) % 2 == 0) drawConnection(i, i + 4)
            if ((i / 8) % 2 == 0) drawConnection(i, i + 8)
        }
    }
}
