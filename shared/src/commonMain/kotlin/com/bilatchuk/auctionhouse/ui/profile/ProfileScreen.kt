package com.bilatchuk.auctionhouse.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import auction_house.shared.generated.resources.Res
import auction_house.shared.generated.resources.lang_en
import auction_house.shared.generated.resources.lang_uk
import auction_house.shared.generated.resources.language
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileScreen(
    currentLanguage: String?,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    Column(
        modifier.fillMaxSize().background(colors.surface).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            stringResource(Res.string.language).uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = colors.primary,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LanguageChip("en", stringResource(Res.string.lang_en), currentLanguage == "en", onLanguageSelected)
            LanguageChip("uk", stringResource(Res.string.lang_uk), currentLanguage == "uk", onLanguageSelected)
        }
    }
}

@Composable
private fun LanguageChip(tag: String, label: String, selected: Boolean, onSelected: (String) -> Unit) {
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
