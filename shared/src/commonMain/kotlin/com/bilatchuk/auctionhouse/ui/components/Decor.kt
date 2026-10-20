package com.bilatchuk.auctionhouse.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bilatchuk.auctionhouse.ui.theme.Noir
import kotlin.math.max

private val FrameGold = Brush.linearGradient(
    0f to Color(0xFF7A5C24),
    0.22f to Color(0xFFF1D98F),
    0.48f to Color(0xFF8D6B2B),
    0.74f to Color(0xFFF7E7B0),
    1f to Color(0xFF6F5220),
)

private const val Diagonal = 1.4142f

private fun DrawScope.drawStripes(spacing: Float, color: Color) {
    val step = spacing * Diagonal
    var x = -size.height
    while (x < size.width) {
        drawLine(color, Offset(x, 0f), Offset(x + size.height, size.height), strokeWidth = 1f)
        x += step
    }
}

private fun DrawScope.drawLattice(spacing: Float, color: Color) {
    drawStripes(spacing, color)
    val step = spacing * Diagonal
    var x = 0f
    while (x < size.width + size.height) {
        drawLine(color, Offset(x, 0f), Offset(x - size.height, size.height), strokeWidth = 1f)
        x += step
    }
}

fun Modifier.wallpaper(): Modifier = drawBehind {
    drawRect(Noir.Background)
    drawLattice(22.dp.toPx(), Noir.Gold.copy(alpha = 0.07f))
    drawRect(
        Brush.radialGradient(
            listOf(Noir.Gold.copy(alpha = 0.2f), Color.Transparent),
            center = Offset(size.width / 2, 0f),
            radius = size.width * 0.6f,
        ),
    )
}

@Composable
fun BackdropPhoto(
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    topShade: Float = 0.3f,
    bottomShade: Float = 0.85f,
) {
    Box(modifier.clipToBounds()) {
        if (painter != null) {
            Image(painter, contentDescription = null, modifier = Modifier.matchParentSize(), contentScale = ContentScale.Crop)
        }
        Box(
            Modifier.matchParentSize().drawBehind {
                val reach = max(size.width, size.height)
                if (painter == null) {
                    drawRect(
                        Brush.radialGradient(
                            listOf(Color(0xFF7A5A2E), Color(0xFF1A130C)),
                            center = Offset(size.width * 0.7f, size.height * 0.25f),
                            radius = reach * 0.9f,
                        ),
                    )
                    drawLattice(18.dp.toPx(), Noir.Gold.copy(alpha = 0.16f))
                    drawRect(
                        Brush.radialGradient(
                            listOf(Color(0x59F1D98F), Color.Transparent),
                            center = Offset(size.width / 2, 0f),
                            radius = size.width * 0.6f,
                        ),
                    )
                }
                drawRect(
                    Brush.verticalGradient(
                        0f to Noir.Background.copy(alpha = topShade),
                        0.85f to Noir.Background.copy(alpha = bottomShade),
                    ),
                )
                drawRect(Brush.radialGradient(listOf(Color.Transparent, Color(0xB3000000)), radius = reach * 0.75f))
            },
        )
    }
}

@Composable
fun PhotoSlot(
    label: String,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    painter: Painter? = null,
) {
    Box(
        modifier
            .clip(shape)
            .drawBehind {
                if (painter == null) {
                    drawRect(
                        Brush.radialGradient(
                            listOf(Color(0xFF6A5639), Color(0xFF1A1612)),
                            center = Offset(size.width / 2, size.height * 0.38f),
                            radius = max(size.width, size.height) * 0.8f,
                        ),
                    )
                    drawStripes(11.dp.toPx(), Color(0x0DFFFFFF))
                    drawRect(
                        Brush.radialGradient(
                            listOf(Color.Transparent, Color(0xBF000000)),
                            radius = max(size.width, size.height) * 0.7f,
                        ),
                    )
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        when {
            painter != null -> Image(painter, null, Modifier.matchParentSize(), contentScale = ContentScale.Crop)
            label.isNotEmpty() -> Text(
                label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = Noir.Gold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp),
            )
            else -> Canvas(Modifier.size(22.dp)) {
                val stroke = Stroke(1.5.dp.toPx())
                drawRoundRect(
                    Noir.Gold,
                    Offset(0f, this.size.height * 0.25f),
                    Size(this.size.width, this.size.height * 0.65f),
                    CornerRadius(3.dp.toPx()),
                    style = stroke,
                )
                drawCircle(Noir.Gold, this.size.width * 0.2f, Offset(this.size.width / 2, this.size.height * 0.575f), style = stroke)
            }
        }
    }
}

private val ArchShape = RoundedCornerShape(topStartPercent = 50, topEndPercent = 50, bottomEndPercent = 0, bottomStartPercent = 0)

@Composable
fun GoldFrame(
    label: String,
    modifier: Modifier = Modifier,
    rim: Dp = 6.dp,
    mat: Dp = 10.dp,
    arch: Boolean = false,
    painter: Painter? = null,
) {
    val shape: Shape = if (arch) ArchShape else RectangleShape
    Box(
        modifier
            .shadow(14.dp, shape, ambientColor = Color.Black, spotColor = Color.Black)
            .background(Noir.Background, shape)
            .border(1.dp, Noir.GildEdge, shape)
            .padding(3.dp)
            .background(FrameGold, shape)
            .padding(rim)
            .background(Noir.Mat, shape)
            .border(1.dp, Color(0x733C280A), shape)
            .padding(mat),
    ) {
        PhotoSlot(label, Modifier.fillMaxSize(), shape, painter)
    }
}
