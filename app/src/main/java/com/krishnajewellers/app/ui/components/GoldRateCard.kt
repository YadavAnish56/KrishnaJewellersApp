package com.krishnajewellers.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishnajewellers.app.data.model.MetalRates
import com.krishnajewellers.app.ui.theme.BorderGold
import com.krishnajewellers.app.ui.theme.CardBg
import com.krishnajewellers.app.ui.theme.DarkText
import com.krishnajewellers.app.ui.theme.DeepGold
import com.krishnajewellers.app.ui.theme.GoldAccent
import com.krishnajewellers.app.ui.theme.MutedText
import com.krishnajewellers.app.ui.theme.PremiumGold
import com.krishnajewellers.app.ui.theme.SecondaryBg

@Composable
fun GoldRateCard(
    rates: MetalRates,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BorderGold, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "TODAY'S PRECIOUS METAL RATES",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp,
                        color = DeepGold
                    )
                    Text(
                        text = "Surat Showroom • ${rates.lastUpdated}",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 11.sp,
                        color = MutedText
                    )
                }

                // Sample Tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(GoldAccent)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "DEMO RATES",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepGold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Rates Grid: 4 boxes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RateItemBox(
                    modifier = Modifier.weight(1f),
                    title = "Gold 24K",
                    subtitle = "per 10g (999)",
                    price = MetalRates.formatInr(rates.gold24kPer10g),
                    highlightColor = PremiumGold
                )
                RateItemBox(
                    modifier = Modifier.weight(1f),
                    title = "Gold 22K",
                    subtitle = "per 10g (916)",
                    price = MetalRates.formatInr(rates.gold22kPer10g),
                    highlightColor = DeepGold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RateItemBox(
                    modifier = Modifier.weight(1f),
                    title = "Gold 18K",
                    subtitle = "per 10g (750)",
                    price = MetalRates.formatInr(rates.gold18kPer10g),
                    highlightColor = PremiumGold
                )
                RateItemBox(
                    modifier = Modifier.weight(1f),
                    title = "Silver 999",
                    subtitle = "per 1 kg",
                    price = MetalRates.formatInr(rates.silverPerKg),
                    highlightColor = Color(0xFF6B7280)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = SecondaryBg, thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))

            // Bottom Disclaimer Note
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MutedText,
                    modifier = Modifier.padding(end = 6.dp).height(14.dp).width(14.dp)
                )
                Text(
                    text = "Owner-entered rates for demonstration. Updated by ${rates.updatedBy}.",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 10.5.sp,
                    color = MutedText
                )
            }
        }
    }
}

@Composable
private fun RateItemBox(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    price: String,
    highlightColor: Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(SecondaryBg)
            .border(0.8.dp, BorderGold, RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .height(8.dp)
                        .width(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(highlightColor)
                )
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = DarkText
                )
            }
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = MutedText,
                modifier = Modifier.padding(top = 1.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = price,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = DarkText
            )
        }
    }
}
