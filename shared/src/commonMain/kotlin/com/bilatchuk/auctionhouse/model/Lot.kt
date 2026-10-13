package com.bilatchuk.auctionhouse.model

import auction_house.shared.generated.resources.*
import org.jetbrains.compose.resources.StringResource

enum class LotCategory(val label: StringResource) {
    ALL(Res.string.cat_all),
    ART(Res.string.cat_art),
    WATCHES(Res.string.cat_watches),
    CARS(Res.string.cat_cars),
    JEWELRY(Res.string.cat_jewelry),
}

enum class LotStatus { CLOSING_SOON, LIVE_NOW, STARTS_SOON, MARQUEE }

enum class NoteKind { RESERVE_MET, PLAIN, ACCENT }

/** Лот каталогу. Текст — ресурси (uk/en); пізніше замінимо на DTO з AuctionHouse API. */
data class Lot(
    val number: Int,
    val category: LotCategory,
    val status: LotStatus,
    val statusLabel: StringResource,
    val timerLabel: StringResource,
    val timerSeconds: Int,
    val place: StringResource,
    val title: StringResource,
    val subtitle: StringResource,
    val italicSubtitle: Boolean = false,
    val tag: StringResource,
    val amountLabel: StringResource,
    val amountUsd: Long,
    val note: StringResource,
    val noteKind: NoteKind,
    val activity: StringResource,
    val activityCount: String? = null,
    val primaryAction: StringResource,
    val primaryActionArg: String? = null,
    val primaryFilled: Boolean,
    val hasSecondaryAction: Boolean,
    val initiallySaved: Boolean = false,
    /** Градієнт-заглушка замість фото (фото підтягнемо з API). */
    val placeholderTop: Long,
    val placeholderBottom: Long,
)

val demoLots = listOf(
    Lot(
        number = 104, category = LotCategory.ART, status = LotStatus.CLOSING_SOON,
        statusLabel = Res.string.status_closing_soon, timerLabel = Res.string.timer_remaining, timerSeconds = 14 * 60 + 32,
        place = Res.string.lot104_place, title = Res.string.lot104_title, subtitle = Res.string.lot104_subtitle,
        italicSubtitle = true, tag = Res.string.lot104_tag,
        amountLabel = Res.string.label_current_high_bid, amountUsd = 1_850_000,
        note = Res.string.reserve_met, noteKind = NoteKind.RESERVE_MET,
        activity = Res.string.bids_placed, activityCount = "19",
        primaryAction = Res.string.act_quick_bid, primaryFilled = true, hasSecondaryAction = true,
        initiallySaved = true, placeholderTop = 0xFF3B2A1E, placeholderBottom = 0xFF1B1410,
    ),
    Lot(
        number = 107, category = LotCategory.JEWELRY, status = LotStatus.LIVE_NOW,
        statusLabel = Res.string.status_live_now, timerLabel = Res.string.timer_final, timerSeconds = 4 * 60 + 55,
        place = Res.string.lot107_place, title = Res.string.lot107_title, subtitle = Res.string.lot107_subtitle,
        tag = Res.string.lot107_tag,
        amountLabel = Res.string.label_current_bid_tier, amountUsd = 3_200_000,
        note = Res.string.paddle, noteKind = NoteKind.ACCENT,
        activity = Res.string.active_bids, activityCount = "27",
        primaryAction = Res.string.act_bid_next, primaryActionArg = "$3,300,000",
        primaryFilled = true, hasSecondaryAction = true,
        placeholderTop = 0xFF14284B, placeholderBottom = 0xFF0A1226,
    ),
    Lot(
        number = 105, category = LotCategory.WATCHES, status = LotStatus.STARTS_SOON,
        statusLabel = Res.string.status_live_in, timerLabel = Res.string.timer_starts_in, timerSeconds = 44 * 60 + 18,
        place = Res.string.lot105_place, title = Res.string.lot105_title, subtitle = Res.string.lot105_subtitle,
        tag = Res.string.lot105_tag,
        amountLabel = Res.string.label_prebid, amountUsd = 420_000,
        note = Res.string.reserve_met, noteKind = NoteKind.RESERVE_MET,
        activity = Res.string.absentee_bids, activityCount = "11",
        primaryAction = Res.string.act_prebid, primaryFilled = false, hasSecondaryAction = false,
        placeholderTop = 0xFF3A2C2C, placeholderBottom = 0xFF171113,
    ),
    Lot(
        number = 106, category = LotCategory.CARS, status = LotStatus.MARQUEE,
        statusLabel = Res.string.status_marquee, timerLabel = Res.string.timer_session, timerSeconds = 2 * 3600 + 15 * 60,
        place = Res.string.lot106_place, title = Res.string.lot106_title, subtitle = Res.string.lot106_subtitle,
        tag = Res.string.lot106_tag,
        amountLabel = Res.string.label_starting_estimate, amountUsd = 38_500_000,
        note = Res.string.est_range, noteKind = NoteKind.PLAIN,
        activity = Res.string.special_vetting, activityCount = null,
        primaryAction = Res.string.act_register, primaryFilled = false, hasSecondaryAction = false,
        placeholderTop = 0xFF4A1414, placeholderBottom = 0xFF1A0A0A,
    ),
)

fun formatUsd(amount: Long): String {
    val digits = amount.toString()
    val grouped = digits.reversed().chunked(3).joinToString(",").reversed()
    return "$$grouped"
}
