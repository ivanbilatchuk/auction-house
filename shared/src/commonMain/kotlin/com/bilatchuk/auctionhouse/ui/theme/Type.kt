package com.bilatchuk.auctionhouse.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import auction_house.shared.generated.resources.Res
import auction_house.shared.generated.resources.inter
import auction_house.shared.generated.resources.playfair_display
import org.jetbrains.compose.resources.Font

@Composable
private fun headingFamily() = FontFamily(Font(Res.font.playfair_display))

@Composable
private fun bodyFamily() = FontFamily(Font(Res.font.inter))

/**
 * Шрифт для цифр (ставки, таймери). У дизайні це JetBrains Mono.
 * Поки використовується Inter з рівною шириною цифр ("tnum");
 * щоб перейти на JetBrains Mono, додайте jetbrains_mono.ttf у composeResources/font і поверніть його тут.
 */
@Composable
private fun numeralFamily() = FontFamily(Font(Res.font.inter))

/** Типографіка з дизайну: Playfair Display + Plus Jakarta Sans (замінено на Inter, бо потрібна кирилиця). */
@Composable
fun auctionTypography(): Typography {
    val heading = headingFamily()
    val body = bodyFamily()
    return Typography(
        displaySmall = TextStyle(fontFamily = heading, fontSize = 32.sp, lineHeight = 40.sp, letterSpacing = (-0.01).em, fontWeight = FontWeight.SemiBold),
        headlineLarge = TextStyle(fontFamily = heading, fontSize = 28.sp, lineHeight = 36.sp, letterSpacing = (-0.01).em, fontWeight = FontWeight.SemiBold),
        headlineMedium = TextStyle(fontFamily = heading, fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.Medium),
        headlineSmall = TextStyle(fontFamily = heading, fontSize = 18.sp, lineHeight = 24.sp, fontWeight = FontWeight.Medium),
        bodyLarge = TextStyle(fontFamily = body, fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Normal),
        bodyMedium = TextStyle(fontFamily = body, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal),
        bodySmall = TextStyle(fontFamily = body, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Normal),
        labelLarge = TextStyle(fontFamily = body, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.04.em, fontWeight = FontWeight.SemiBold),
        labelMedium = TextStyle(fontFamily = body, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.06.em, fontWeight = FontWeight.SemiBold),
        labelSmall = TextStyle(fontFamily = body, fontSize = 10.sp, lineHeight = 14.sp, letterSpacing = 0.08.em, fontWeight = FontWeight.SemiBold),
    )
}

/** Цифрові стилі дизайну: numeral-sm / md / lg / xl. */
class AuctionNumerals(val sm: TextStyle, val md: TextStyle, val lg: TextStyle, val xl: TextStyle)

@Composable
fun auctionNumerals(): AuctionNumerals {
    val family = numeralFamily()
    fun style(size: Int, line: Int, weight: FontWeight, spacing: Float = 0f) = TextStyle(
        fontFamily = family,
        fontSize = size.sp,
        lineHeight = line.sp,
        letterSpacing = spacing.em,
        fontWeight = weight,
        fontFeatureSettings = "tnum",
    )
    return AuctionNumerals(
        sm = style(12, 16, FontWeight.Medium),
        md = style(16, 22, FontWeight.Medium),
        lg = style(24, 30, FontWeight.Medium),
        xl = style(32, 38, FontWeight.SemiBold, -0.02f),
    )
}

val LocalAuctionNumerals = staticCompositionLocalOf<AuctionNumerals> {
    error("AuctionTheme is not provided")
}

object AuctionStyles {
    val numerals: AuctionNumerals
        @Composable @ReadOnlyComposable get() = LocalAuctionNumerals.current
}
