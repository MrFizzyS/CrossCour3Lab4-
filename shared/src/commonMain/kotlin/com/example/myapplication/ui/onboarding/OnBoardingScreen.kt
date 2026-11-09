package com.example.myapplication.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.generated.resources.*
import com.example.myapplication.ui.components.ChecklistIndicator
import com.example.myapplication.ui.components.PaperSheet
import com.example.myapplication.ui.theme.LocalPaper
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private data class OnboardingPage(
    val title: StringResource,
    val description: StringResource
)

// Кількість слайдів має збігатися з OnboardingState.pageCount.
// Індекс слайда задає тип анімації в MiniTodoSheet (0, 1, 2).
private val pages = listOf(
    OnboardingPage(Res.string.onb_title_1, Res.string.onb_desc_1),
    OnboardingPage(Res.string.onb_title_2, Res.string.onb_desc_2),
    OnboardingPage(Res.string.onb_title_3, Res.string.onb_desc_3)
)

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val paper = LocalPaper.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { state.pageCount })

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                OnboardingEffect.NavigateToAuth -> onFinished()
            }
        }
    }
    // State -> Pager (кнопка «Далі»)
    LaunchedEffect(state.page) {
        if (pagerState.currentPage != state.page) pagerState.animateScrollToPage(state.page)
    }
    // Pager -> State (свайп)
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            viewModel.onIntent(OnboardingIntent.PageChanged(it))
        }
    }

    Box(
        Modifier.fillMaxSize().background(paper.desk).safeDrawingPadding().padding(12.dp)
    ) {
        PaperSheet(Modifier.fillMaxSize()) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { viewModel.onIntent(OnboardingIntent.Skip) }) {
                    Text(stringResource(Res.string.skip), color = paper.ink)
                }
            }

            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { index ->
                val page = pages[index]
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    MiniTodoSheet(kind = index, active = pagerState.currentPage == index)
                    Spacer(Modifier.height(32.dp))
                    Text(
                        stringResource(page.title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = paper.ink,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        stringResource(page.description),
                        style = MaterialTheme.typography.bodyLarge,
                        color = paper.ink.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            ChecklistIndicator(
                pageCount = state.pageCount,
                currentPage = state.page,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 16.dp)
            )

            Button(
                onClick = { viewModel.onIntent(OnboardingIntent.Next) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = paper.ink,
                    contentColor = paper.paper
                )
            ) {
                Text(stringResource(if (state.isLast) Res.string.start else Res.string.next))
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}