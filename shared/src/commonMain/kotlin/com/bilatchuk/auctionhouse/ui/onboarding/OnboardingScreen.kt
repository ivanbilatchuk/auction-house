package com.bilatchuk.auctionhouse.ui.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import auction_house.shared.generated.resources.Res
import auction_house.shared.generated.resources.app_name
import auction_house.shared.generated.resources.back
import auction_house.shared.generated.resources.next
import auction_house.shared.generated.resources.onb_tag_1
import auction_house.shared.generated.resources.onb_tag_2
import auction_house.shared.generated.resources.onb_tag_3
import auction_house.shared.generated.resources.onb_tag_4
import auction_house.shared.generated.resources.onb_text_1
import auction_house.shared.generated.resources.onb_text_2
import auction_house.shared.generated.resources.onb_text_3
import auction_house.shared.generated.resources.onb_text_4
import auction_house.shared.generated.resources.onb_title_1
import auction_house.shared.generated.resources.onb_title_2
import auction_house.shared.generated.resources.onb_title_3
import auction_house.shared.generated.resources.onb_title_4
import auction_house.shared.generated.resources.photo_hall
import auction_house.shared.generated.resources.photo_painting
import auction_house.shared.generated.resources.photo_stamps
import auction_house.shared.generated.resources.photo_talar
import auction_house.shared.generated.resources.skip
import auction_house.shared.generated.resources.start
import co.touchlab.kermit.Logger
import com.bilatchuk.auctionhouse.ui.components.BackdropPhoto
import com.bilatchuk.auctionhouse.ui.components.Eyebrow
import com.bilatchuk.auctionhouse.ui.components.GoldButton
import com.bilatchuk.auctionhouse.ui.components.GoldFrame
import com.bilatchuk.auctionhouse.ui.components.OutlineButton
import com.bilatchuk.auctionhouse.ui.components.wallpaper
import com.bilatchuk.auctionhouse.ui.theme.Noir
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val log = Logger.withTag("Onboarding")

private enum class SlideFrame(val ratio: Float, val arch: Boolean = false, val tilt: Float = 0f) {
    Square(0.85f),
    Wide(1.45f),
    Arch(0.68f, arch = true),
    Tall(0.74f, tilt = -5f),
}

private data class OnboardingPage(
    val tag: StringResource,
    val title: StringResource,
    val text: StringResource,
    val photo: DrawableResource,
    val frame: SlideFrame,
)

private val pages = listOf(
    OnboardingPage(Res.string.onb_tag_1, Res.string.onb_title_1, Res.string.onb_text_1, Res.drawable.photo_talar, SlideFrame.Square),
    OnboardingPage(Res.string.onb_tag_2, Res.string.onb_title_2, Res.string.onb_text_2, Res.drawable.photo_hall, SlideFrame.Wide),
    OnboardingPage(Res.string.onb_tag_3, Res.string.onb_title_3, Res.string.onb_text_3, Res.drawable.photo_painting, SlideFrame.Arch),
    OnboardingPage(Res.string.onb_tag_4, Res.string.onb_title_4, Res.string.onb_text_4, Res.drawable.photo_stamps, SlideFrame.Tall),
)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pager = rememberPagerState { pages.size }
    val scope = rememberCoroutineScope()
    val last = pager.currentPage == pages.lastIndex

    LaunchedEffect(pager.currentPage) {
        log.d { "Onboarding page ${pager.currentPage + 1}/${pages.size}" }
    }

    BoxWithConstraints(Modifier.fillMaxSize().wallpaper()) {
        val wide = maxWidth >= 840.dp
        val pad = if (wide) 72.dp else 24.dp

        BackdropPhoto(Modifier.matchParentSize(), topShade = 0.35f, bottomShade = 0.94f)

        Column(Modifier.fillMaxSize().systemBarsPadding()) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = pad, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    stringResource(Res.string.app_name).uppercase(),
                    style = MaterialTheme.typography.titleLarge.copy(letterSpacing = 6.sp),
                )
                Box(
                    Modifier.heightIn(min = 44.dp).clickable(role = Role.Button, onClick = onFinish).padding(horizontal = 4.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        stringResource(Res.string.skip).uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        color = Noir.Muted,
                    )
                }
            }

            HorizontalPager(state = pager, modifier = Modifier.weight(1f)) { index ->
                Slide(pages[index], index, wide, pad)
            }

            Row(
                Modifier.fillMaxWidth().padding(horizontal = pad, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Dots(pager) { target -> scope.launch { pager.animateScrollToPage(target) } }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (wide && pager.currentPage > 0) {
                        OutlineButton(
                            stringResource(Res.string.back),
                            { scope.launch { pager.animateScrollToPage(pager.currentPage - 1) } },
                        )
                    }
                    GoldButton(
                        text = stringResource(if (last) Res.string.start else Res.string.next),
                        onClick = {
                            if (last) onFinish() else scope.launch { pager.animateScrollToPage(pager.currentPage + 1) }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun Slide(page: OnboardingPage, index: Int, wide: Boolean, pad: Dp) {
    val number = "0${index + 1} / 0${pages.size}"
    val text: @Composable () -> Unit = {
        Column(verticalArrangement = Arrangement.spacedBy(if (wide) 22.dp else 14.dp)) {
            Eyebrow(number, trailing = stringResource(page.tag))
            Text(
                stringResource(page.title),
                style = if (wide) MaterialTheme.typography.displayLarge else MaterialTheme.typography.displayMedium,
            )
            Text(
                stringResource(page.text),
                style = MaterialTheme.typography.bodyLarge,
                color = Noir.Muted,
                modifier = Modifier.widthIn(max = 440.dp),
            )
        }
    }
    if (wide) {
        Row(Modifier.fillMaxSize()) {
            Box(Modifier.weight(1f).fillMaxHeight().padding(horizontal = pad), contentAlignment = Alignment.CenterStart) { text() }
            Box(Modifier.weight(1f).fillMaxHeight()) {
                BackdropPhoto(Modifier.matchParentSize(), topShade = 0.15f, bottomShade = 0.7f)
                PageArt(page, Modifier.matchParentSize())
            }
        }
    } else {
        Column(Modifier.fillMaxSize()) {
            PageArt(page, Modifier.fillMaxWidth().weight(1f))
            Box(Modifier.fillMaxWidth().padding(horizontal = pad)) { text() }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun PageArt(page: OnboardingPage, modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        GoldFrame(
            "",
            Modifier.fillMaxHeight(0.78f).aspectRatio(page.frame.ratio, matchHeightConstraintsFirst = true).rotate(page.frame.tilt),
            rim = 8.dp,
            mat = 16.dp,
            arch = page.frame.arch,
            painter = painterResource(page.photo),
        )
    }
}

@Composable
private fun Dots(pager: PagerState, onSelect: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(pager.pageCount) { i ->
            val active = pager.currentPage == i
            val w = animateDpAsState(if (active) 26.dp else 10.dp).value
            Box(
                Modifier.heightIn(min = 44.dp).clickable(role = Role.Button) { onSelect(i) }.padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(Modifier.width(w).height(4.dp).background(if (active) Noir.Gold else Noir.GoldLine))
            }
        }
    }
}
