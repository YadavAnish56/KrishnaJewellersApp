package com.krishnajewellers.app.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishnajewellers.app.ui.theme.BorderGold
import com.krishnajewellers.app.ui.theme.CardBg
import com.krishnajewellers.app.ui.theme.DarkText
import com.krishnajewellers.app.ui.theme.DeepGold
import com.krishnajewellers.app.ui.theme.GoldAccent
import com.krishnajewellers.app.ui.theme.MutedText
import com.krishnajewellers.app.ui.theme.PremiumGold

data class NavigationTabItem(
    val id: String,
    val title: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val badgeCount: Int = 0
)

@Composable
fun LuxuryBottomNavigationBar(
    currentTab: String,
    onTabSelected: (String) -> Unit,
    savedCount: Int = 0,
    unreadEnquiries: Int = 0,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavigationTabItem("home", "Home", Icons.Filled.Home, Icons.Outlined.Home),
        NavigationTabItem("explore", "Explore", Icons.Filled.Explore, Icons.Outlined.Explore),
        NavigationTabItem("saved", "Saved", Icons.Filled.Bookmark, Icons.Outlined.BookmarkBorder, badgeCount = savedCount),
        NavigationTabItem("enquiries", "Enquiries", Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline, badgeCount = unreadEnquiries),
        NavigationTabItem("profile", "Profile", Icons.Filled.Person, Icons.Outlined.PersonOutline)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(CardBg)
            .border(width = 0.8.dp, color = BorderGold.copy(alpha = 0.6f))
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentTab == item.id
                val interactionSource = remember { MutableInteractionSource() }

                val animatedIndicatorWidth by animateDpAsState(
                    targetValue = if (isSelected) 28.dp else 0.dp,
                    animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
                    label = "IndicatorWidth"
                )

                val tintColor by animateColorAsState(
                    targetValue = if (isSelected) DeepGold else MutedText,
                    label = "TabColor"
                )

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { onTabSelected(item.id) }
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top gold indicator line
                    Box(
                        modifier = Modifier
                            .height(2.5.dp)
                            .width(animatedIndicatorWidth)
                            .clip(CircleShape)
                            .background(if (isSelected) PremiumGold else Color.Transparent)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    BadgedBox(
                        badge = {
                            if (item.badgeCount > 0) {
                                Badge(
                                    containerColor = PremiumGold,
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = item.badgeCount.toString(),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.filledIcon else item.outlinedIcon,
                            contentDescription = item.title,
                            tint = tintColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = item.title,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        fontSize = 10.sp,
                        color = tintColor
                    )
                }
            }
        }
    }
}
