package com.krishnajewellers.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishnajewellers.app.R
import com.krishnajewellers.app.ui.theme.DarkText
import com.krishnajewellers.app.ui.theme.DeepGold
import com.krishnajewellers.app.ui.theme.GoldAccent
import com.krishnajewellers.app.ui.theme.MutedText
import com.krishnajewellers.app.ui.theme.PremiumGold

@Composable
fun KJLogoBadge(
    modifier: Modifier = Modifier,
    size: Dp = 44.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color(0xFF181512))
            .border(1.5.dp, PremiumGold, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "Krishna Jewellers Logo",
            modifier = Modifier.size(size)
        )
    }
}

@Composable
fun KJBrandHeader(
    modifier: Modifier = Modifier,
    logoSize: Dp = 72.dp,
    showSubtitle: Boolean = true
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        KJLogoBadge(size = logoSize)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "KRISHNA JEWELLERS",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            letterSpacing = 2.sp,
            color = DarkText
        )

        if (showSubtitle) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "TIMELESS ELEGANCE • SURAT",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                letterSpacing = 1.5.sp,
                color = PremiumGold
            )
        }
    }
}
