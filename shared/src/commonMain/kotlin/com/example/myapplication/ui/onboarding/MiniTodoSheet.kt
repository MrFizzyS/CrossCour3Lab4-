package com.example.myapplication.ui.onboarding

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.LocalPaper

private val barWidths = listOf(0.85f, 0.6f, 0.75f)

private fun seg(t: Float, from: Float, to: Float): Float =
    ((t - from) / (to - from)).coerceIn(0f, 1f)

/**
 * Стікер-список з анімацією.
 * kind 0 — рядки з'являються, 1 — ставляться галочки, 2 — прогрес 100% і рядок «викидається».
 */
@Composable
fun MiniTodoSheet(kind: Int, active: Boolean, modifier: Modifier = Modifier) {
    val paper = LocalPaper.current
    val note = if (paper.paper.luminance() > 0.5f) Color(0xFFFFF1A8) else Color(0xFF5B5330)

    // Анімація запускається щоразу, коли слайд стає активним
    val anim = remember { Animatable(0f) }
    LaunchedEffect(active) {
        anim.snapTo(0f)
        if (active) anim.animateTo(1f, tween(durationMillis = 2200, easing = LinearEasing))
    }
    val t = anim.value

    val appear = List(3) { i -> if (kind == 0) seg(t, i * 0.2f, i * 0.2f + 0.3f) else 1f }
    val checked = List(3) { i ->
        when (kind) {
            0 -> 0f
            1 -> seg(t, 0.15f + i * 0.25f, 0.35f + i * 0.25f)
            else -> seg(t, i * 0.1f, i * 0.1f + 0.15f)
        }
    }
    val thrown = if (kind == 2) seg(t, 0.7f, 1f) else 0f
    val fill = when (kind) {
        0 -> 0f
        1 -> checked.sum() / 3f
        else -> seg(t, 0.2f, 0.65f)
    }

    Box(modifier.width(280.dp), contentAlignment = Alignment.TopCenter) {
        Column(
            Modifier
                .fillMaxWidth()
                .graphicsLayer { rotationZ = -2f }
                .shadow(8.dp, RoundedCornerShape(6.dp))
                .background(note, RoundedCornerShape(6.dp))
                .padding(start = 20.dp, end = 20.dp, top = 28.dp, bottom = 20.dp)
        ) {
            // Заголовок стікера
            Box(
                Modifier
                    .fillMaxWidth(0.55f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(paper.ink)
            )
            Spacer(Modifier.height(10.dp))

            repeat(3) { i ->
                MiniRow(
                    widthFraction = barWidths[i],
                    appear = appear[i],
                    checked = checked[i],
                    thrown = if (i == 0) thrown else 0f
                )
            }

            // Шкала прогресу
            Spacer(Modifier.height(14.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(paper.rule)
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(fill)
                        .fillMaxHeight()
                        .background(paper.ink)
                )
            }
        }

        // «Скотч» зверху стікера
        Box(
            Modifier
                .offset(y = (-6).dp)
                .width(64.dp)
                .height(20.dp)
                .graphicsLayer { rotationZ = 3f }
                .background(paper.margin.copy(alpha = 0.45f))
        )
    }
}

@Composable
private fun MiniRow(widthFraction: Float, appear: Float, checked: Float, thrown: Float) {
    val paper = LocalPaper.current
    Column(
        Modifier
            .fillMaxWidth()
            .graphicsLayer {
                alpha = appear * (1f - thrown)
                translationX = (1f - appear) * -24.dp.toPx() + thrown * 220.dp.toPx()
                rotationZ = thrown * 18f
            }
    ) {
        Row(
            Modifier.fillMaxWidth().height(44.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(20.dp).border(1.5.dp, paper.ink, RoundedCornerShape(3.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("✓", color = paper.ink, fontSize = 14.sp, modifier = Modifier.alpha(checked))
            }
            Spacer(Modifier.width(12.dp))
            Box(Modifier.weight(1f).height(14.dp), contentAlignment = Alignment.CenterStart) {
                // «Текст» завдання
                Box(
                    Modifier
                        .fillMaxWidth(widthFraction)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(paper.ink.copy(alpha = 0.55f - 0.3f * checked))
                )
                // Лінія закреслення
                Box(
                    Modifier
                        .fillMaxWidth(widthFraction * checked)
                        .height(2.dp)
                        .background(paper.ink)
                )
            }
        }
        HorizontalDivider(color = paper.rule)
    }
}