package com.example.myapplication.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import com.example.myapplication.generated.resources.Res
import com.example.myapplication.generated.resources.completed_of
import com.example.myapplication.generated.resources.todos_title
import com.example.myapplication.ui.theme.BrandGradient
import com.example.myapplication.ui.theme.BrandTealLight
import com.example.myapplication.generated.resources.test_onboarding
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProgressHeader(
    done: Int,
    total: Int,
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    onShowOnboarding: () -> Unit
) {
    val target = if (total == 0) 0f else done / total.toFloat()
    val progress by animateFloatAsState(target)

    Column(
        Modifier
            .fillMaxWidth()
            .background(
                BrandGradient,
                RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            )
            .statusBarsPadding()
            .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 28.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(Res.string.todos_title),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🌙")
                Spacer(Modifier.width(8.dp))
                Switch(
                    checked = darkTheme,
                    onCheckedChange = onDarkThemeChange,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = BrandTealLight,
                        checkedThumbColor = Color.White
                    )
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        Text(
            stringResource(Res.string.completed_of, done, total),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.9f)
        )
        Spacer(Modifier.height(12.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(50)),
            color = BrandTealLight,
            trackColor = Color.White.copy(alpha = 0.25f)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedButton(
            onClick = onShowOnboarding,
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.7f)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text(stringResource(Res.string.test_onboarding))
        }
    }
}