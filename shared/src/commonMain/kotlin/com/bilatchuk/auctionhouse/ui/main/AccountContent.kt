package com.bilatchuk.auctionhouse.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import auction_house.shared.generated.resources.Res
import auction_house.shared.generated.resources.lang_en
import auction_house.shared.generated.resources.lang_uk
import auction_house.shared.generated.resources.language
import auction_house.shared.generated.resources.nav_account
import com.bilatchuk.auctionhouse.ui.components.Eyebrow
import com.bilatchuk.auctionhouse.ui.components.GoldChip
import org.jetbrains.compose.resources.stringResource

@Composable
fun AccountContent(wide: Boolean, currentLanguage: String?, onLanguageSelected: (String) -> Unit) {
    val pad = if (wide) 56.dp else 20.dp
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = pad, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Text(stringResource(Res.string.nav_account), style = MaterialTheme.typography.displayMedium)
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Eyebrow(stringResource(Res.string.language))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                GoldChip(stringResource(Res.string.lang_en), currentLanguage == "en", { onLanguageSelected("en") })
                GoldChip(stringResource(Res.string.lang_uk), currentLanguage == "uk", { onLanguageSelected("uk") })
            }
        }
    }
}
