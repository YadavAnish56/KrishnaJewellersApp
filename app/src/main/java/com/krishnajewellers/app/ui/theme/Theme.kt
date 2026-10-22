package com.krishnajewellers.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PremiumGold,
    onPrimary = CardBg,
    primaryContainer = GoldAccent,
    onPrimaryContainer = DeepGold,
    secondary = DeepGold,
    onSecondary = CardBg,
    secondaryContainer = SecondaryBg,
    onSecondaryContainer = DarkText,
    tertiary = SoftGold,
    onTertiary = DarkText,
    background = PrimaryBg,
    onBackground = DarkText,
    surface = CardBg,
    onSurface = DarkText,
    surfaceVariant = SecondaryBg,
    onSurfaceVariant = MutedText,
    outline = BorderGold,
    outlineVariant = GoldAccent
)

@Composable
fun KrishnaJewellersTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = PrimaryBg.toArgb()
                window.navigationBarColor = PrimaryBg.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = true
                controller.isAppearanceLightNavigationBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
