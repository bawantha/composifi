package com.bawantha.composifi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bawantha.composifi.ui.theme.ComposifiTheme

@Composable
fun RateMeDialog(
    onDismissRequest: () -> Unit,
    onRateNowClick: () -> Unit,
    onLaterClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxWidth()) {
                ConfettiAnimation(modifier = Modifier.matchParentSize())
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Attach the Shimmering Star Animation here
                ShimmeringStarAnimation(
                    pulseRadius = 60f,
                    modifier = Modifier.padding(16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                CoolAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                FloatingHeartsAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                LiquidWaveAnimation(modifier = Modifier.fillMaxWidth().height(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                InternetCoolAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                CssFireAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                ParticleTornadoAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                PlasmaWaveAnimation(modifier = Modifier.fillMaxWidth().height(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                FirefliesAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                SpirographAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                NeonHexagonAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                SpinningCubeAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                DnaHelixAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                NeonRingAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                ConstellationAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                HypnoticSpiralAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                AtomAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                GalaxySpiralAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                RadarSweepAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                ParticleSwarmAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                WarpSpeedAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                FireworkAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                BlackHoleAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                MatrixRainAnimation(modifier = Modifier.fillMaxWidth().height(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                LissajousCurveAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Donut3DAnimation(modifier = Modifier.size(100.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Enjoying the App?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "If you like our animations, please take a moment to rate us. It really helps!",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = onLaterClick) {
                        Text("Later")
                    }
                    Button(onClick = onRateNowClick) {
                        Text("Rate Now")
                    }
                }
            }
            }
        }
    }
}

@Preview
@Composable
fun RateMeDialogPreview() {
    ComposifiTheme {
        RateMeDialog(
            onDismissRequest = {},
            onRateNowClick = {},
            onLaterClick = {}
        )
    }
}
