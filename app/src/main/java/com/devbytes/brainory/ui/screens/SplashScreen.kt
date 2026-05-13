package com.devbytes.brainory.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devbytes.brainory.R
import com.devbytes.brainory.ui.theme.BrainoryAppTheme
import kotlinx.coroutines.delay

private val gradientBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFFFFFAC),
        Color(0xFFBDCFCD),
        Color(0xFF87A9FF)
    )
)

private val TaglineColor = Color(0xFF1A1A2E)
private const val SPLASH_DURATION_MS = 2_000L

@Composable
fun SplashScreen(onSplashFinished: () -> Unit = {}) {
    LaunchedEffect(Unit) {
        delay(SPLASH_DURATION_MS)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.brainory_logo),
                contentDescription = "Brainory Logo",
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "BRAINORY",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A2E),
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Student Study Tracker & Journal",
                fontSize = 14.sp,
                color = TaglineColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(
    name = "Splash Screen",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
private fun SplashScreenPreview() {
    BrainoryAppTheme {
        SplashScreen()
    }
}