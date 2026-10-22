package com.krishnajewellers.app.ui.screens.saved

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishnajewellers.app.ui.components.JewelleryProductCard
import com.krishnajewellers.app.ui.components.LuxuryEmptyState
import com.krishnajewellers.app.ui.theme.BorderGold
import com.krishnajewellers.app.ui.theme.CardBg
import com.krishnajewellers.app.ui.theme.DarkText
import com.krishnajewellers.app.ui.theme.DeepGold
import com.krishnajewellers.app.ui.theme.MutedText
import com.krishnajewellers.app.ui.theme.PrimaryBg
import com.krishnajewellers.app.ui.theme.PremiumGold
import com.krishnajewellers.app.viewmodel.SavedViewModel

@Composable
fun SavedScreen(
    viewModel: SavedViewModel,
    onNavigateToProduct: (String) -> Unit,
    onNavigateToExplore: () -> Unit,
    onNavigateToEnquiryChat: (String) -> Unit
) {
    val savedProducts by viewModel.savedProducts.collectAsState()
    val isGridView by viewModel.isGridView.collectAsState()
    val rates by viewModel.rates.collectAsState()
    val likedIds by viewModel.likedProductIds.collectAsState()
    val savedIds by viewModel.savedProductIds.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBg)
            .statusBarsPadding()
    ) {
        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "SAVED JEWELLERY",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    letterSpacing = 1.sp,
                    color = DarkText
                )
                Text(
                    text = "${savedProducts.size} items bookmarked in your private collection",
                    fontSize = 11.5.sp,
                    color = MutedText
                )
            }

            if (savedProducts.isNotEmpty()) {
                IconButton(
                    onClick = { viewModel.toggleLayout() },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardBg)
                        .border(1.dp, BorderGold, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = if (isGridView) Icons.Default.ViewList else Icons.Default.GridView,
                        contentDescription = "Toggle View",
                        tint = DeepGold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (savedProducts.isEmpty()) {
            LuxuryEmptyState(
                icon = Icons.Default.BookmarkBorder,
                title = "Your Saved Collection is Empty",
                description = "Tap the bookmark icon on any piece in our gold or silver catalogue to save it here for convenient reference or showroom consultation.",
                actionButtonText = "Explore Catalogue",
                onActionClick = onNavigateToExplore
            )
        } else {
            if (isGridView) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 6.dp, bottom = 90.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(savedProducts, key = { it.id }) { product ->
                        JewelleryProductCard(
                            product = product,
                            rates = rates,
                            isLiked = likedIds.contains(product.id),
                            isSaved = true,
                            onProductClick = { onNavigateToProduct(product.id) },
                            onLikeClick = { viewModel.toggleLiked(product.id) },
                            onSaveClick = { viewModel.removeSaved(product.id) },
                            onEnquireClick = {
                                val convId = viewModel.startEnquiry(product)
                                onNavigateToEnquiryChat(convId)
                            }
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(savedProducts, key = { it.id }) { product ->
                        JewelleryProductCard(
                            product = product,
                            rates = rates,
                            isLiked = likedIds.contains(product.id),
                            isSaved = true,
                            onProductClick = { onNavigateToProduct(product.id) },
                            onLikeClick = { viewModel.toggleLiked(product.id) },
                            onSaveClick = { viewModel.removeSaved(product.id) },
                            onEnquireClick = {
                                val convId = viewModel.startEnquiry(product)
                                onNavigateToEnquiryChat(convId)
                            }
                        )
                    }
                }
            }
        }
    }
}
