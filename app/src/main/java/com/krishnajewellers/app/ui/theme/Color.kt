package com.krishnajewellers.app.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Brand Luxury Palette
val PrimaryBg = Color(0xFFFFFCF7)
val SecondaryBg = Color(0xFFF7F1E7)
val PremiumGold = Color(0xFFB89448)
val DeepGold = Color(0xFF8A682C)
val LightGold = Color(0xFFD4AF37)
val SoftGold = Color(0xFFDFC89B)
val GoldAccent = Color(0xFFEEE1C7)
val DarkText = Color(0xFF24201B)
val MutedText = Color(0xFF81796F)
val CardBg = Color(0xFFFFFFFF)
val BorderGold = Color(0xFFE2D5BA)

// Status & Helper Colors
val StatusPending = Color(0xFFE69B00)
val StatusQuoted = Color(0xFF2F80ED)
val StatusAccepted = Color(0xFF27AE60)
val StatusDeclined = Color(0xFFEB5757)

val SilverAccent = Color(0xFF9EA3A8)
val SilverLight = Color(0xFFE8ECEF)
val SilverDark = Color(0xFF5D646B)

val ShimmerColor = Color(0xFFF0E8D8)

// Gradients
val GoldGradient = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF8A682C),
        Color(0xFFB89448),
        Color(0xFFD4AF37),
        Color(0xFF8A682C)
    )
)

val SoftGoldGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFFFF9EE),
        Color(0xFFF7EED9)
    )
)

val HeroOverlayGradient = Brush.verticalGradient(
    colors = listOf(
        Color.Transparent,
        Color(0x9924201B),
        Color(0xDD181410)
    )
)
