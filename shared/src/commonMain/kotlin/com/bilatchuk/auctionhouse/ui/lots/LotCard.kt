package com.bilatchuk.auctionhouse.ui.lots

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import auction_house.shared.generated.resources.Res
import auction_house.shared.generated.resources.lot_label
import auction_house.shared.generated.resources.timer_format
import auction_house.shared.generated.resources.usd
import co.touchlab.kermit.Logger
import com.bilatchuk.auctionhouse.model.Lot
import com.bilatchuk.auctionhouse.model.LotStatus
import com.bilatchuk.auctionhouse.model.NoteKind
import com.bilatchuk.auctionhouse.model.formatUsd
import com.bilatchuk.auctionhouse.ui.components.AppIcons
import com.bilatchuk.auctionhouse.ui.components.PulsingDot
import com.bilatchuk.auctionhouse.ui.components.rememberCountdown
import com.bilatchuk.auctionhouse.ui.theme.AuctionStyles
import org.jetbrains.compose.resources.stringResource

private val log = Logger.withTag("LotCard")

private fun Int.pad2() = toString().padStart(2, '0')

@Composable
fun LotCard(lot: Lot, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    var saved by remember { mutableStateOf(lot.initiallySaved) }
    val secondsLeft = rememberCountdown(lot.timerSeconds)

    Column(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(colors.surfaceContainerLow)
    ) {
        // ---------- Медіа-блок 4:3 ----------
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f)
                .background(colors.surfaceContainerLowest)
        ) {
            // Заглушка замість фото + номер лота
            Box(
                Modifier.fillMaxSize().background(
                    Brush.verticalGradient(listOf(Color(lot.placeholderTop), Color(lot.placeholderBottom)))
                ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = lot.number.toString(),
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 96.sp),
                    color = colors.primary.copy(alpha = 0.12f),
                )
            }
            // Віньєтка
            Box(
                Modifier.fillMaxSize().background(
                    Brush.verticalGradient(
                        listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent, colors.surfaceContainerLow)
                    )
                )
            )

            StatusBadge(lot, Modifier.align(Alignment.TopStart).padding(12.dp))

            // Закладка
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(32.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.surfaceContainerLowest.copy(alpha = 0.7f))
                    .clickable {
                        saved = !saved
                        log.i { "Lot ${lot.number} watchlist = $saved" }
                    },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    AppIcons.Bookmark, null, Modifier.size(18.dp),
                    tint = if (saved) colors.primary else colors.onSurfaceVariant,
                )
            }

            // Плаваюча панель таймера
            val urgent = lot.status == LotStatus.LIVE_NOW
            val timerColor = when (lot.status) {
                LotStatus.CLOSING_SOON, LotStatus.LIVE_NOW -> colors.primary
                else -> colors.onSurface
            }
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(12.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(colors.surfaceContainerLowest.copy(alpha = 0.9f))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    Modifier.weight(1f, fill = false),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        AppIcons.Schedule, null, Modifier.size(16.dp),
                        tint = when {
                            urgent -> colors.error
                            lot.status == LotStatus.CLOSING_SOON -> colors.primary
                            else -> colors.outline
                        },
                    )
                    Text(
                        stringResource(lot.timerLabel).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (urgent) colors.error else colors.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    stringResource(
                        Res.string.timer_format,
                        (secondsLeft / 3600).pad2(),
                        ((secondsLeft % 3600) / 60).pad2(),
                        (secondsLeft % 60).pad2(),
                    ),
                    style = AuctionStyles.numerals.sm.copy(letterSpacing = 0.05.em),
                    color = timerColor,
                    fontWeight = if (urgent) FontWeight.Bold else FontWeight.Medium,
                    maxLines = 1,
                )
            }
        }

        // ---------- Метадані ----------
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            stringResource(Res.string.lot_label, lot.number.toString()).uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.1.em),
                            color = colors.primary,
                        )
                        Box(Modifier.size(4.dp).background(colors.outline, CircleShape))
                        Text(
                            stringResource(lot.place),
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Text(
                        stringResource(lot.title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = colors.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        stringResource(lot.subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant,
                        fontStyle = if (lot.italicSubtitle) FontStyle.Italic else FontStyle.Normal,
                    )
                }
                Text(
                    stringResource(lot.tag),
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.onSurfaceVariant,
                    modifier = Modifier
                        .clip(RoundedCornerShape(2.dp))
                        .background(colors.surfaceContainerHigh)
                        .padding(horizontal = 8.dp, vertical = 2.dp),
                )
            }

            BidRail(lot)

            // Дії
            Row(Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val actionText = stringResource(lot.primaryAction, *listOfNotNull(lot.primaryActionArg).toTypedArray())
                Box(
                    Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (lot.primaryFilled) colors.primary else colors.surfaceContainerHigh)
                        .clickable { log.i { "Action '$actionText' on lot ${lot.number}" } }
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        actionText.uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        color = if (lot.primaryFilled) colors.onPrimary else colors.primary,
                        fontWeight = if (lot.primaryFilled) FontWeight.Bold else FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                if (lot.hasSecondaryAction) {
                    Box(
                        Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(colors.surfaceContainer)
                            .clickable { log.i { "More on lot ${lot.number}" } },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            if (lot.status == LotStatus.LIVE_NOW) AppIcons.Info else AppIcons.MoreVert,
                            null, Modifier.size(20.dp), tint = colors.onSurface,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(lot: Lot, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val (background, content) = when (lot.status) {
        LotStatus.CLOSING_SOON -> colors.surfaceContainerLowest.copy(alpha = 0.85f) to colors.error
        LotStatus.LIVE_NOW -> colors.errorContainer to colors.onErrorContainer
        LotStatus.STARTS_SOON -> colors.surfaceContainerLowest.copy(alpha = 0.85f) to colors.onSurface
        LotStatus.MARQUEE -> colors.primaryContainer to colors.onPrimaryContainer
    }
    Row(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        when (lot.status) {
            LotStatus.CLOSING_SOON -> PulsingDot(colors.error)
            LotStatus.LIVE_NOW -> PulsingDot(colors.onErrorContainer)
            LotStatus.STARTS_SOON -> PulsingDot(colors.secondary, pulsing = false)
            LotStatus.MARQUEE -> Icon(AppIcons.Star, null, Modifier.size(14.dp), tint = content)
        }
        Text(
            stringResource(lot.statusLabel).uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = content,
            fontWeight = if (lot.status == LotStatus.LIVE_NOW || lot.status == LotStatus.MARQUEE) FontWeight.Bold else FontWeight.SemiBold,
        )
    }
}

@Composable
private fun BidRail(lot: Lot) {
    val colors = MaterialTheme.colorScheme
    val numerals = AuctionStyles.numerals
    val highlighted = lot.status != LotStatus.STARTS_SOON

    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(colors.surfaceContainer)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                stringResource(lot.amountLabel).uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = colors.onSurfaceVariant,
            )
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    formatUsd(lot.amountUsd),
                    style = numerals.lg,
                    fontWeight = FontWeight.Bold,
                    color = if (highlighted) colors.primary else colors.onSurface,
                    maxLines = 1,
                )
                Text(
                    stringResource(Res.string.usd),
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }
        }
        Spacer(Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                if (lot.noteKind == NoteKind.RESERVE_MET) {
                    Icon(AppIcons.CheckCircle, null, Modifier.size(14.dp), tint = colors.secondary)
                }
                Text(
                    stringResource(lot.note),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (lot.noteKind == NoteKind.PLAIN) colors.onSurfaceVariant else colors.secondary,
                )
            }
            val activityText = stringResource(lot.activity, *listOfNotNull(lot.activityCount).toTypedArray())
            if (lot.activityCount != null) {
                Text(activityText, style = numerals.sm, color = colors.onSurfaceVariant)
            } else {
                Text(activityText, style = MaterialTheme.typography.labelSmall, color = colors.tertiary)
            }
        }
    }
}
