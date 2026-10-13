package com.bilatchuk.auctionhouse.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import auction_house.shared.generated.resources.Res
import auction_house.shared.generated.resources.next
import auction_house.shared.generated.resources.onb_text_1
import auction_house.shared.generated.resources.onb_text_2
import auction_house.shared.generated.resources.onb_text_3
import auction_house.shared.generated.resources.onb_text_4
import auction_house.shared.generated.resources.onb_title_1
import auction_house.shared.generated.resources.onb_title_2
import auction_house.shared.generated.resources.onb_title_3
import auction_house.shared.generated.resources.onb_title_4
import auction_house.shared.generated.resources.skip
import auction_house.shared.generated.resources.start
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private val log = Logger.withTag("Onboarding")

private data class OnboardingPage(val title: StringResource, val text: StringResource)

private val pages = listOf(
    OnboardingPage(Res.string.onb_title_1, Res.string.onb_text_1),
    OnboardingPage(Res.string.onb_title_2, Res.string.onb_text_2),
    OnboardingPage(Res.string.onb_title_3, Res.string.onb_text_3),
    OnboardingPage(Res.string.onb_title_4, Res.string.onb_text_4),
)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState { pages.size }
    val scope = rememberCoroutineScope()
    val isLast = pagerState.currentPage == pages.lastIndex

    LaunchedEffect(pagerState.currentPage) {
        log.d { "Onboarding page ${pagerState.currentPage + 1}/${pages.size}" }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding()
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = onFinish) { Text(stringResource(Res.string.skip)) }
        }

        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { index ->
            val page = pages[index]
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Золоте число як «ілюстрація» слайда — без залежності від емодзі/іконок
                Text(
                    text = "0${index + 1}",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Spacer(Modifier.height(24.dp))
                Text(
                    text = stringResource(page.title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(page.text),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )
            }
        }

        Row(
            Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(pages.size) { i ->
                val selected = pagerState.currentPage == i
                Box(
                    Modifier
                        .padding(4.dp)
                        .size(if (selected) 12.dp else 8.dp)
                        .background(
                            if (selected) MaterialTheme.colorScheme.secondary
                            else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.25f),
                            CircleShape,
                        )
                )
            }
        }

        Button(
            onClick = {
                if (isLast) onFinish()
                else scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(if (isLast) Res.string.start else Res.string.next))
        }
    }
}
