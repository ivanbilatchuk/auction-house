package com.bilatchuk.auctionhouse

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import co.touchlab.kermit.Logger
import com.bilatchuk.auctionhouse.data.AppSettings
import com.bilatchuk.auctionhouse.ui.main.MainScreen
import com.bilatchuk.auctionhouse.ui.onboarding.OnboardingScreen
import com.bilatchuk.auctionhouse.ui.theme.AuctionTheme

private val log = Logger.withTag("AuctionHouse")

@Composable
@Preview
fun App() {
    val settings = remember { AppSettings() }
    var language by remember {
        val saved = settings.language
        saved?.let { applyAppLanguage(it) }
        mutableStateOf(saved)
    }
    var showOnboarding by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) { log.d { "App started, onboarding=$showOnboarding, language=$language" } }

    key(language) {
        AuctionTheme {
            if (showOnboarding) {
                OnboardingScreen(onFinish = { showOnboarding = false })
            } else {
                MainScreen(
                    currentLanguage = language,
                    onLanguageSelected = { tag ->
                        settings.language = tag
                        applyAppLanguage(tag)
                        language = tag
                    },
                )
            }
        }
    }
}
