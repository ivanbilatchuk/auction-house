package com.bilatchuk.auctionhouse.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import auction_house.shared.generated.resources.Res
import auction_house.shared.generated.resources.app_name
import auction_house.shared.generated.resources.lang_en
import auction_house.shared.generated.resources.lang_uk
import auction_house.shared.generated.resources.language
import auction_house.shared.generated.resources.main_description
import auction_house.shared.generated.resources.main_subtitle
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen(
    currentLanguage: String?,
    onLanguageSelected: (String) -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    Column(
        Modifier
            .fillMaxSize()
            .background(colors.background)
            .safeContentPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            stringResource(Res.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = colors.primary,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            stringResource(Res.string.main_subtitle),
            style = MaterialTheme.typography.headlineSmall,
            color = colors.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            stringResource(Res.string.main_description),
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(40.dp))
        Text(
            stringResource(Res.string.language).uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = colors.secondary,
        )
        Spacer(Modifier.height(12.dp))
        Row {
            LanguageChip("en", stringResource(Res.string.lang_en), currentLanguage == "en", onLanguageSelected)
            Spacer(Modifier.width(8.dp))
            LanguageChip("uk", stringResource(Res.string.lang_uk), currentLanguage == "uk", onLanguageSelected)
        }
    }
}

@Composable
private fun LanguageChip(
    tag: String,
    label: String,
    selected: Boolean,
    onSelected: (String) -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    Text(
        label.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = if (selected) colors.onPrimary else colors.onSurfaceVariant,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) colors.primary else colors.surfaceContainerLow)
            .clickable { onSelected(tag) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
    )
}
