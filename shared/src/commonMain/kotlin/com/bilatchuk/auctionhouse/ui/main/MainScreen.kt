package com.bilatchuk.auctionhouse.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import auction_house.shared.generated.resources.Res
import auction_house.shared.generated.resources.app_name
import auction_house.shared.generated.resources.nav_account
import auction_house.shared.generated.resources.nav_catalog
import auction_house.shared.generated.resources.nav_home
import auction_house.shared.generated.resources.stub_soon
import co.touchlab.kermit.Logger
import com.bilatchuk.auctionhouse.ui.components.wallpaper
import com.bilatchuk.auctionhouse.ui.theme.Noir
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private val log = Logger.withTag("Navigation")

private enum class Section(val label: StringResource) {
    Home(Res.string.nav_home),
    Catalog(Res.string.nav_catalog),
    Account(Res.string.nav_account),
}

private val WideBreakpoint = 840.dp

@Composable
fun MainScreen(
    currentLanguage: String?,
    onLanguageSelected: (String) -> Unit,
) {
    var section by remember { mutableStateOf(Section.Home) }

    LaunchedEffect(section) { log.d { "Section: $section" } }

    BoxWithConstraints(Modifier.fillMaxSize().wallpaper()) {
        val wide = maxWidth >= WideBreakpoint

        Column(Modifier.fillMaxSize().statusBarsPadding()) {
            if (wide) TopNav(section, onNavigate = { section = it })
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                Box(Modifier.widthIn(max = 1280.dp).fillMaxSize()) {
                    when (section) {
                        Section.Home -> HomeContent(wide, onOpenCatalog = { section = Section.Catalog })
                        Section.Catalog -> StubContent(section)
                        Section.Account -> AccountContent(wide, currentLanguage, onLanguageSelected)
                    }
                }
            }
            if (!wide) BottomNav(section, onNavigate = { section = it })
        }
    }
}

@Composable
private fun StubContent(section: Section) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(section.label), style = MaterialTheme.typography.displayMedium)
        Text(stringResource(Res.string.stub_soon), style = MaterialTheme.typography.bodyLarge, color = Noir.Muted)
    }
}

@Composable
private fun TopNav(section: Section, onNavigate: (Section) -> Unit) {
    Column {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 56.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(Res.string.app_name).uppercase(),
                style = MaterialTheme.typography.titleLarge.copy(letterSpacing = 6.sp),
                modifier = Modifier.clickable { onNavigate(Section.Home) },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(36.dp)) {
                Section.entries.forEach { target ->
                    val active = section == target
                    Box(
                        Modifier
                            .heightIn(min = 44.dp)
                            .drawBehind {
                                if (active) {
                                    val line = 2.dp.toPx()
                                    drawRect(Noir.Gold, Offset(0f, size.height - line), Size(size.width, line))
                                }
                            }
                            .clickable(role = Role.Tab) { onNavigate(target) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            stringResource(target.label).uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = if (active) Noir.Gold else Noir.Muted,
                        )
                    }
                }
            }
        }
        Box(Modifier.fillMaxWidth().height(1.dp).background(Noir.GoldLine))
    }
}

@Composable
private fun BottomNav(section: Section, onNavigate: (Section) -> Unit) {
    Column(Modifier.background(Noir.Background).navigationBarsPadding()) {
        Box(Modifier.fillMaxWidth().height(1.dp).background(Noir.GoldLine))
        Row(Modifier.fillMaxWidth()) {
            Section.entries.forEach { item ->
                val active = section == item
                Column(
                    Modifier.weight(1f).heightIn(min = 56.dp).clickable(role = Role.Tab) { onNavigate(item) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Box(Modifier.fillMaxWidth(0.4f).height(2.dp).background(if (active) Noir.Gold else Noir.Background))
                    Box(Modifier.height(10.dp))
                    Text(
                        stringResource(item.label).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (active) Noir.Gold else Noir.Muted,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}
