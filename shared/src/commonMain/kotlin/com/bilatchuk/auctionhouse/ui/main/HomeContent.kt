package com.bilatchuk.auctionhouse.ui.main

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import auction_house.shared.generated.resources.Res
import auction_house.shared.generated.resources.cat_antiques
import auction_house.shared.generated.resources.cat_art
import auction_house.shared.generated.resources.cat_coins
import auction_house.shared.generated.resources.cat_stamps
import auction_house.shared.generated.resources.ends_in
import auction_house.shared.generated.resources.home_categories
import auction_house.shared.generated.resources.home_cta
import auction_house.shared.generated.resources.home_ending
import auction_house.shared.generated.resources.home_eyebrow
import auction_house.shared.generated.resources.home_text
import auction_house.shared.generated.resources.home_title
import auction_house.shared.generated.resources.lot_painting
import auction_house.shared.generated.resources.lot_stamps
import auction_house.shared.generated.resources.lot_talar
import auction_house.shared.generated.resources.photo_hall
import auction_house.shared.generated.resources.photo_painting
import auction_house.shared.generated.resources.photo_stamps
import auction_house.shared.generated.resources.photo_talar
import auction_house.shared.generated.resources.time_dh
import auction_house.shared.generated.resources.time_hm
import com.bilatchuk.auctionhouse.ui.components.BackdropPhoto
import com.bilatchuk.auctionhouse.ui.components.Eyebrow
import com.bilatchuk.auctionhouse.ui.components.GoldButton
import com.bilatchuk.auctionhouse.ui.components.GoldFrame
import com.bilatchuk.auctionhouse.ui.theme.Noir
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private data class HomeLot(
    val title: StringResource,
    val photo: DrawableResource,
    val price: String,
    val days: Int,
    val hours: Int,
)

private val categories = listOf(
    Res.string.cat_coins,
    Res.string.cat_stamps,
    Res.string.cat_art,
    Res.string.cat_antiques,
)

private val endingLots = listOf(
    HomeLot(Res.string.lot_talar, Res.drawable.photo_talar, "₴ 18 400", 0, 5),
    HomeLot(Res.string.lot_stamps, Res.drawable.photo_stamps, "₴ 3 200", 1, 3),
    HomeLot(Res.string.lot_painting, Res.drawable.photo_painting, "₴ 52 000", 2, 14),
)

@Composable
fun HomeContent(wide: Boolean, onOpenCatalog: () -> Unit) {
    val pad = if (wide) 56.dp else 20.dp
    Column(
        Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = pad, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        Hero(wide, onOpenCatalog)

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(stringResource(Res.string.home_categories), style = MaterialTheme.typography.headlineMedium)
            val tile = @Composable { name: StringResource, modifier: Modifier ->
                Row(
                    modifier.border(1.dp, Noir.GoldLine).padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    GoldFrame("", Modifier.width(56.dp).height(54.dp), rim = 3.dp, mat = 4.dp)
                    Text(stringResource(name), style = MaterialTheme.typography.titleMedium)
                }
            }
            if (wide) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    categories.forEach { tile(it, Modifier.weight(1f)) }
                }
            } else {
                categories.chunked(2).forEach { pair ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        pair.forEach { tile(it, Modifier.weight(1f)) }
                    }
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(stringResource(Res.string.home_ending), style = MaterialTheme.typography.headlineMedium)
            if (wide) {
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    endingLots.forEach { LotTile(it, 240.dp, Modifier.weight(1f)) }
                }
            } else {
                Row(
                    Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    endingLots.forEach { LotTile(it, 170.dp, Modifier.width(170.dp)) }
                }
            }
        }
    }
}

@Composable
private fun Hero(wide: Boolean, onOpenCatalog: () -> Unit) {
    Box(Modifier.fillMaxWidth().border(1.dp, Noir.GoldLine)) {
        BackdropPhoto(Modifier.matchParentSize(), painterResource(Res.drawable.photo_hall), topShade = 0.45f, bottomShade = 0.9f)
        Column(
            Modifier.padding(if (wide) 48.dp else 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Eyebrow(stringResource(Res.string.home_eyebrow))
            Text(
                stringResource(Res.string.home_title),
                style = if (wide) MaterialTheme.typography.displayLarge else MaterialTheme.typography.displayMedium,
            )
            Text(
                stringResource(Res.string.home_text),
                style = MaterialTheme.typography.bodyLarge,
                color = Noir.Muted,
                modifier = Modifier.width(if (wide) 480.dp else 320.dp),
            )
            GoldButton(stringResource(Res.string.home_cta), onOpenCatalog)
        }
    }
}

@Composable
private fun LotTile(lot: HomeLot, plateHeight: androidx.compose.ui.unit.Dp, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        GoldFrame("", Modifier.fillMaxWidth().height(plateHeight), rim = 5.dp, mat = 8.dp, painter = painterResource(lot.photo))
        Text(stringResource(lot.title), style = MaterialTheme.typography.titleMedium, minLines = 2, maxLines = 2)
        Text(lot.price, style = MaterialTheme.typography.bodyMedium, color = Noir.Gold)
        val left = if (lot.days > 0) {
            stringResource(Res.string.time_dh, lot.days.toString(), lot.hours.toString())
        } else {
            stringResource(Res.string.time_hm, lot.hours.toString(), "00")
        }
        Text(stringResource(Res.string.ends_in, left), style = MaterialTheme.typography.bodySmall, color = Noir.Muted)
    }
}
