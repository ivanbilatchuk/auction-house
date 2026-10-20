package com.bilatchuk.auctionhouse.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val AuctionColors = darkColorScheme(
    primary = Noir.Gold, onPrimary = Noir.Background,
    primaryContainer = Noir.Gold, onPrimaryContainer = Noir.Background,
    secondary = Noir.Gold, onSecondary = Noir.Background,
    secondaryContainer = Noir.SurfaceHigh, onSecondaryContainer = Noir.Ink,
    tertiary = Noir.Burgundy, onTertiary = Noir.Ink,
    background = Noir.Background, onBackground = Noir.Ink,
    surface = Noir.Surface, onSurface = Noir.Ink,
    surfaceVariant = Noir.SurfaceHigh, onSurfaceVariant = Noir.Muted,
    surfaceTint = Noir.Gold,
    error = Noir.Danger, onError = Noir.OnDanger,
    outline = Noir.GoldLineStrong, outlineVariant = Noir.GoldLine,
    scrim = Color.Black,
    surfaceDim = Noir.Background,
    surfaceBright = Noir.SurfaceHigh,
    surfaceContainerLowest = Noir.Background,
    surfaceContainerLow = Noir.Surface,
    surfaceContainer = Noir.Surface,
    surfaceContainerHigh = Noir.SurfaceHigh,
    surfaceContainerHighest = Noir.SurfaceHigh,
)

private val AuctionShapes = Shapes(
    extraSmall = RoundedCornerShape(0.dp),
    small = RoundedCornerShape(0.dp),
    medium = RoundedCornerShape(2.dp),
    large = RoundedCornerShape(2.dp),
    extraLarge = RoundedCornerShape(4.dp),
)

@Composable
fun AuctionTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AuctionColors,
        typography = auctionTypography(),
        shapes = AuctionShapes,
    ) {
        CompositionLocalProvider(LocalContentColor provides Noir.Ink, content = content)
    }
}
