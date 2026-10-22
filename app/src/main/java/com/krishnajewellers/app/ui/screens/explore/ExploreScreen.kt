package com.krishnajewellers.app.ui.screens.explore

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishnajewellers.app.data.model.MetalType
import com.krishnajewellers.app.ui.components.CategoryFilterChip
import com.krishnajewellers.app.ui.components.JewelleryProductCard
import com.krishnajewellers.app.ui.components.LuxuryEmptyState
import com.krishnajewellers.app.ui.theme.BorderGold
import com.krishnajewellers.app.ui.theme.CardBg
import com.krishnajewellers.app.ui.theme.DarkText
import com.krishnajewellers.app.ui.theme.DeepGold
import com.krishnajewellers.app.ui.theme.GoldAccent
import com.krishnajewellers.app.ui.theme.MutedText
import com.krishnajewellers.app.ui.theme.PrimaryBg
import com.krishnajewellers.app.ui.theme.PremiumGold
import com.krishnajewellers.app.ui.theme.SecondaryBg
import com.krishnajewellers.app.viewmodel.ExploreViewModel
import com.krishnajewellers.app.viewmodel.SortOrder

@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel,
    onNavigateToProduct: (String) -> Unit,
    onNavigateToEnquiryChat: (String) -> Unit
) {
    val selectedMetal by viewModel.selectedMetal.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val products by viewModel.filteredProducts.collectAsState()
    val rates by viewModel.rates.collectAsState()
    val savedIds by viewModel.savedProductIds.collectAsState()
    val likedIds by viewModel.likedProductIds.collectAsState()

    var showSortMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBg)
            .statusBarsPadding()
    ) {
        // Screen Header & Search Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CATALOGUE",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    letterSpacing = 1.sp,
                    color = DarkText
                )

                // Gold / Silver Toggle Switch
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(SecondaryBg)
                        .border(1.dp, BorderGold, RoundedCornerShape(12.dp))
                        .padding(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9.dp))
                            .background(if (selectedMetal == MetalType.GOLD) DeepGold else Color.Transparent)
                            .clickable { viewModel.selectMetal(MetalType.GOLD) }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "GOLD",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp,
                            color = if (selectedMetal == MetalType.GOLD) Color.White else DarkText
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(9.dp))
                            .background(if (selectedMetal == MetalType.SILVER) Color(0xFF5D646B) else Color.Transparent)
                            .clickable { viewModel.selectMetal(MetalType.SILVER) }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "SILVER",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp,
                            color = if (selectedMetal == MetalType.SILVER) Color.White else DarkText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar & Sort Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    placeholder = { Text("Search by name, category, or purity...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = DeepGold)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = MutedText)
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepGold,
                        unfocusedBorderColor = BorderGold,
                        focusedContainerColor = CardBg,
                        unfocusedContainerColor = CardBg
                    ),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box {
                    IconButton(
                        onClick = { showSortMenu = true },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(CardBg)
                            .border(1.dp, BorderGold, RoundedCornerShape(14.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Sort",
                            tint = DeepGold
                        )
                    }

                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Popular / Featured") },
                            onClick = {
                                viewModel.setSortOrder(SortOrder.POPULAR)
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Price: Low to High") },
                            onClick = {
                                viewModel.setSortOrder(SortOrder.PRICE_LOW_TO_HIGH)
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Price: High to Low") },
                            onClick = {
                                viewModel.setSortOrder(SortOrder.PRICE_HIGH_TO_LOW)
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Weight: Low to High") },
                            onClick = {
                                viewModel.setSortOrder(SortOrder.WEIGHT_LOW_TO_HIGH)
                                showSortMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Category Filter Chips Carousel
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    CategoryFilterChip(
                        title = cat,
                        isSelected = cat == selectedCategory,
                        onClick = { viewModel.selectCategory(cat) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Product Grid or Empty State
        if (products.isEmpty()) {
            LuxuryEmptyState(
                title = "No Jewellery Found",
                description = "We couldn't find any designs matching \"$searchQuery\" in $selectedCategory. Try adjusting your search keywords.",
                actionButtonText = "Reset Filters",
                onActionClick = {
                    viewModel.onSearchQueryChanged("")
                    viewModel.selectCategory(if (selectedMetal == MetalType.GOLD) "All Gold" else "All Silver")
                }
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 90.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(products, key = { it.id }) { product ->
                    JewelleryProductCard(
                        product = product,
                        rates = rates,
                        isLiked = likedIds.contains(product.id),
                        isSaved = savedIds.contains(product.id),
                        onProductClick = { onNavigateToProduct(product.id) },
                        onLikeClick = { viewModel.toggleLiked(product.id) },
                        onSaveClick = { viewModel.toggleSaved(product.id) },
                        onEnquireClick = {
                            // Start enquiry and navigate to chat
                            onNavigateToProduct(product.id)
                        }
                    )
                }
            }
        }
    }
}
