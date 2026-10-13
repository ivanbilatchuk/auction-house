package com.bilatchuk.auctionhouse.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import auction_house.shared.generated.resources.Res
import auction_house.shared.generated.resources.app_name
import auction_house.shared.generated.resources.coming_soon
import auction_house.shared.generated.resources.header_subtitle
import auction_house.shared.generated.resources.nav_auctions
import auction_house.shared.generated.resources.nav_bids
import auction_house.shared.generated.resources.nav_profile
import auction_house.shared.generated.resources.nav_watchlist
import co.touchlab.kermit.Logger
import com.bilatchuk.auctionhouse.ui.components.AppIcons
import com.bilatchuk.auctionhouse.ui.components.PulsingDot
import com.bilatchuk.auctionhouse.ui.lots.LotsScreen
import com.bilatchuk.auctionhouse.ui.profile.ProfileScreen
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private val log = Logger.withTag("Navigation")

private enum class Tab(val label: StringResource, val icon: ImageVector) {
    AUCTIONS(Res.string.nav_auctions, AppIcons.List),
    WATCHLIST(Res.string.nav_watchlist, AppIcons.Bookmark),
    BIDS(Res.string.nav_bids, AppIcons.Money),
    PROFILE(Res.string.nav_profile, AppIcons.Person),
}

@Composable
fun MainScreen(
    currentLanguage: String?,
    onLanguageSelected: (String) -> Unit,
) {
    var tab by remember { mutableStateOf(Tab.AUCTIONS) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = { AuctionTopBar() },
        bottomBar = {
            AuctionBottomBar(selected = tab, onSelect = {
                log.d { "Tab = $it" }
                tab = it
            })
        },
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (tab) {
                Tab.AUCTIONS -> LotsScreen()
                Tab.PROFILE -> ProfileScreen(currentLanguage, onLanguageSelected)
                else -> ComingSoon()
            }
        }
    }
}

@Composable
private fun AuctionTopBar() {
    val colors = MaterialTheme.colorScheme
    Column(
        Modifier
            .fillMaxWidth()
            .shadow(8.dp)
            .background(colors.surfaceContainerLowest.copy(alpha = 0.95f))
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Row(
            Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Емблема (у дизайні — зображення)
                Box(
                    Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(colors.primaryContainer, colors.primary))),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("A", style = MaterialTheme.typography.headlineSmall, color = colors.onPrimary)
                }
                Column {
                    Text(
                        stringResource(Res.string.app_name),
                        style = MaterialTheme.typography.headlineSmall,
                        color = colors.primary,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        stringResource(Res.string.header_subtitle).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.onSurfaceVariant,
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colors.surfaceContainerHigh.copy(alpha = 0.6f))
                        .clickable { log.d { "Notifications clicked" } },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(AppIcons.Notifications, null, Modifier.size(22.dp), tint = colors.onSurface)
                    Box(Modifier.align(Alignment.TopEnd).padding(10.dp)) { PulsingDot(colors.primary, size = 8.dp) }
                }
                // Аватар-заглушка
                Box(
                    Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(colors.primaryContainer.copy(alpha = 0.6f), colors.surfaceContainerHigh))),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(AppIcons.Person, null, Modifier.size(20.dp), tint = colors.onSurface)
                }
            }
        }
    }
}

@Composable
private fun AuctionBottomBar(selected: Tab, onSelect: (Tab) -> Unit) {
    val colors = MaterialTheme.colorScheme
    Row(
        Modifier
            .fillMaxWidth()
            .shadow(16.dp)
            .background(colors.surfaceContainerLowest.copy(alpha = 0.95f))
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(80.dp)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Tab.entries.forEach { item ->
            val active = item == selected
            Column(
                Modifier
                    .widthIn(min = 64.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onSelect(item) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    item.icon, null, Modifier.size(24.dp),
                    tint = if (active) colors.primary else colors.onSurfaceVariant,
                )
                Text(
                    stringResource(item.label).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (active) colors.primary else colors.onSurfaceVariant,
                    fontWeight = if (active) FontWeight.Bold else FontWeight.SemiBold,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun ComingSoon() {
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface), contentAlignment = Alignment.Center) {
        Text(
            stringResource(Res.string.coming_soon),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
