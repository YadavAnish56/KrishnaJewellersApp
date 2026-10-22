package com.krishnajewellers.app.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishnajewellers.app.data.model.MetalRates
import com.krishnajewellers.app.ui.components.KJLogoBadge
import com.krishnajewellers.app.ui.theme.BorderGold
import com.krishnajewellers.app.ui.theme.CardBg
import com.krishnajewellers.app.ui.theme.DarkText
import com.krishnajewellers.app.ui.theme.DeepGold
import com.krishnajewellers.app.ui.theme.GoldAccent
import com.krishnajewellers.app.ui.theme.MutedText
import com.krishnajewellers.app.ui.theme.PrimaryBg
import com.krishnajewellers.app.ui.theme.PremiumGold
import com.krishnajewellers.app.ui.theme.SecondaryBg
import com.krishnajewellers.app.viewmodel.AdminViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: AdminViewModel,
    onBackClick: () -> Unit,
    onNavigateManageRates: () -> Unit,
    onNavigateManageProducts: () -> Unit,
    onNavigateManageEnquiries: () -> Unit,
    onNavigateManageCustomRequests: () -> Unit
) {
    val stats by viewModel.stats.collectAsState()
    val rates by viewModel.rates.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBg)
            .statusBarsPadding()
    ) {
        // Admin Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(CardBg)
                    .border(1.dp, BorderGold, CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DarkText)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "SHOWROOM OWNER DESK",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 0.5.sp,
                    color = DarkText
                )
                Text(
                    text = "Krishna Jewellers • Surat Master Admin",
                    fontSize = 11.sp,
                    color = DeepGold
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF24201B))
                    .border(1.dp, PremiumGold, RoundedCornerShape(10.dp))
                    .clickable { onBackClick() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Exit Portal",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Demo Mode Notice
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(SecondaryBg)
                        .border(1.dp, BorderGold, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = DeepGold,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Simplified offline store manager. Changes to rates and products take effect instantly across customer screens.",
                            fontSize = 12.sp,
                            color = DarkText,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Overview Metric Stats Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Catalogue",
                        count = "${stats.totalProducts}",
                        subtitle = "Active Pieces",
                        icon = Icons.Default.Inventory2,
                        accentColor = PremiumGold
                    )
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Enquiries",
                        count = "${stats.totalEnquiries}",
                        subtitle = "Customer Chats",
                        icon = Icons.Default.ChatBubble,
                        accentColor = DeepGold
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Custom Orders",
                        count = "${stats.totalCustomRequests}",
                        subtitle = "${stats.pendingQuotesCount} Pending Quotes",
                        icon = Icons.Default.DesignServices,
                        accentColor = Color(0xFF2F80ED)
                    )
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "22K Gold Rate",
                        count = MetalRates.formatInr(rates.gold22kPer10g),
                        subtitle = "per 10g",
                        icon = Icons.Default.CurrencyRupee,
                        accentColor = PremiumGold
                    )
                }
            }

            // Management Action Rows
            item {
                Text(
                    text = "STORE MANAGEMENT ACTIONS",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    letterSpacing = 1.sp,
                    color = DeepGold
                )
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderGold, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg)
                ) {
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        AdminActionRow(
                            icon = Icons.Default.PriceChange,
                            title = "Manage Precious Metal Rates",
                            subtitle = "Update 24K, 22K, 18K gold and silver rates",
                            onClick = onNavigateManageRates
                        )
                        androidx.compose.material3.HorizontalDivider(color = SecondaryBg, thickness = 1.dp)

                        AdminActionRow(
                            icon = Icons.Default.Inventory2,
                            title = "Manage Jewellery Catalogue",
                            subtitle = "Add new products, edit weights, or hide listings",
                            onClick = onNavigateManageProducts
                        )
                        androidx.compose.material3.HorizontalDivider(color = SecondaryBg, thickness = 1.dp)

                        AdminActionRow(
                            icon = Icons.Default.ChatBubble,
                            title = "Customer Enquiries & Replies",
                            subtitle = "View customer messages and reply from showroom",
                            onClick = onNavigateManageEnquiries
                        )
                        androidx.compose.material3.HorizontalDivider(color = SecondaryBg, thickness = 1.dp)

                        AdminActionRow(
                            icon = Icons.Default.DesignServices,
                            title = "Review Custom Gold Requests",
                            subtitle = "Inspect photos, enter formal quotes, update status",
                            onClick = onNavigateManageCustomRequests
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier = Modifier,
    title: String,
    count: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color
) {
    Card(
        modifier = modifier
            .border(1.dp, BorderGold, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MutedText
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = count,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DarkText
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = MutedText
            )
        }
    }
}

@Composable
private fun AdminActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SecondaryBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = DeepGold, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DarkText
                )
                Text(
                    text = subtitle,
                    fontSize = 11.5.sp,
                    color = MutedText
                )
            }
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MutedText, modifier = Modifier.size(20.dp))
    }
}
