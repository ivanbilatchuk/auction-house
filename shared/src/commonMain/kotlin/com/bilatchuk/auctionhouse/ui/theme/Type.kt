package com.bilatchuk.auctionhouse.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
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
fun auctionTypography(): Typography {
    val heading = FontFamily(Font(Res.font.playfair_display))
    val body = FontFamily(Font(Res.font.inter))
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
