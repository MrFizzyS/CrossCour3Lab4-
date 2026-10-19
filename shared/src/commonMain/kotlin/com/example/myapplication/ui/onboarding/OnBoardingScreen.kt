package com.example.myapplication.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.generated.resources.*
import com.example.myapplication.ui.components.PageIndicator
import com.example.myapplication.ui.theme.BrandGradient
import com.example.myapplication.ui.theme.BrandTealLight
import com.example.myapplication.ui.theme.SoftGradient
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private data class OnboardingPage(
    val emoji: String,
    val title: StringResource,
    val description: StringResource
)

private val pages = listOf(
    OnboardingPage("👋", Res.string.onb_title_1, Res.string.onb_desc_1),
    OnboardingPage("📝", Res.string.onb_title_2, Res.string.onb_desc_2),
    OnboardingPage("🚀", Res.string.onb_title_3, Res.string.onb_desc_3)
)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val isLast = pagerState.currentPage == pages.lastIndex

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = onFinish) { Text(stringResource(Res.string.skip)) }
        }

        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { index ->
            val page = pages[index]
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(Modifier.size(260.dp), contentAlignment = Alignment.Center) {
                    Box(
                        Modifier
                            .size(220.dp)
                            .background(BrandGradient, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(page.emoji, fontSize = 96.sp)
                    }
                    Box(
                        Modifier
                            .size(44.dp)
                            .align(Alignment.TopEnd)
                            .background(SoftGradient, CircleShape)
                    )
                    Box(
                        Modifier
                            .size(28.dp)
                            .align(Alignment.BottomStart)
                            .background(BrandTealLight, CircleShape)
                    )
                }
                Spacer(Modifier.height(36.dp))
                Text(
                    stringResource(page.title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    stringResource(page.description),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }

        PageIndicator(
            pageCount = pages.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier.padding(vertical = 20.dp)
        )

        Button(
            onClick = {
                if (isLast) onFinish()
                else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            Text(stringResource(if (isLast) Res.string.start else Res.string.next))
        }
    }
}