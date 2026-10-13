package com.bilatchuk.auctionhouse.ui.lots

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import auction_house.shared.generated.resources.Res
import auction_house.shared.generated.resources.footer_text
import auction_house.shared.generated.resources.footer_title
import auction_house.shared.generated.resources.nothing_found
import auction_house.shared.generated.resources.search_hint
import auction_house.shared.generated.resources.ticker_live
import co.touchlab.kermit.Logger
import com.bilatchuk.auctionhouse.model.LotCategory
import com.bilatchuk.auctionhouse.model.demoLots
import com.bilatchuk.auctionhouse.ui.components.AppIcons
import com.bilatchuk.auctionhouse.ui.components.PulsingDot
import com.bilatchuk.auctionhouse.ui.theme.AuctionStyles
import org.jetbrains.compose.resources.stringResource

private val log = Logger.withTag("LotsScreen")

@Composable
fun LotsScreen(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    var query by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(LotCategory.ALL) }

    // Рядки для пошуку (з урахуванням поточної мови)
    val searchable = demoLots.associateWith { lot ->
        listOf(
            lot.number.toString(),
            stringResource(lot.title),
            stringResource(lot.subtitle),
            stringResource(lot.place),
        ).joinToString(" ").lowercase()
    }
    val visible = demoLots.filter { lot ->
        (category == LotCategory.ALL || lot.category == category) &&
            (query.isBlank() || searchable.getValue(lot).contains(query.trim().lowercase()))
    }

    LazyColumn(
        modifier.fillMaxSize().background(colors.surface),
        contentPadding = PaddingValues(bottom = 8.dp),
    ) {
        // Стрічка статусу залу
        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(colors.surfaceContainerLowest)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PulsingDot(colors.primary, size = 10.dp)
                    Text(
                        stringResource(Res.string.ticker_live).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.primary,
                        maxLines = 1,
                    )
                }
                Row(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.surfaceContainerHigh)
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(AppIcons.Visibility, null, Modifier.size(14.dp), tint = colors.onSurfaceVariant)
                    Text("1,428", style = AuctionStyles.numerals.sm, color = colors.onSurfaceVariant)
                }
            }
        }

        // Пошук і фільтр
        item {
            Row(
                Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(4.dp))
                        .background(colors.surfaceContainerLow)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(AppIcons.Search, null, Modifier.size(18.dp), tint = colors.outline)
                    Box(Modifier.weight(1f)) {
                        if (query.isEmpty()) {
                            Text(
                                stringResource(Res.string.search_hint),
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.outline,
                                maxLines = 1,
                            )
                        }
                        BasicTextField(
                            value = query,
                            onValueChange = { query = it },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodySmall.copy(color = colors.onSurface),
                            cursorBrush = SolidColor(colors.primary),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
                Box(
                    Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(colors.surfaceContainerLow)
                        .clickable { log.d { "Filter clicked" } },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(AppIcons.Tune, null, Modifier.size(20.dp), tint = colors.onSurface)
                }
            }
        }

        // Категорії
        item {
            Row(
                Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                LotCategory.entries.forEach { item ->
                    val selected = item == category
                    Text(
                        stringResource(item.label).uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = MaterialTheme.typography.labelSmall.letterSpacing),
                        color = if (selected) colors.onPrimary else colors.onSurfaceVariant,
                        maxLines = 1,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) colors.primary else colors.surfaceContainerLow)
                            .clickable {
                                category = item
                                log.d { "Category = $item" }
                            }
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                    )
                }
            }
        }

        // Каталог
        if (visible.isEmpty()) {
            item {
                Text(
                    stringResource(Res.string.nothing_found),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                )
            }
        } else {
            items(visible, key = { it.number }) { lot ->
                LotCard(lot, Modifier.padding(horizontal = 16.dp, vertical = 12.dp))
            }
        }

        // Підвал
        item {
            Column(
                Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(AppIcons.CheckCircle, null, Modifier.size(28.dp), tint = colors.outline)
                Text(
                    stringResource(Res.string.footer_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = colors.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                Text(
                    stringResource(Res.string.footer_text),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.outline,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
            }
        }
    }
}
