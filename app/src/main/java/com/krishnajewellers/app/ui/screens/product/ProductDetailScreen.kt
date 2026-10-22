package com.krishnajewellers.app.ui.screens.product

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishnajewellers.app.data.model.MetalRates
import com.krishnajewellers.app.ui.components.PremiumGoldButton
import com.krishnajewellers.app.ui.components.PriceBreakdownCard
import com.krishnajewellers.app.ui.theme.BorderGold
import com.krishnajewellers.app.ui.theme.CardBg
import com.krishnajewellers.app.ui.theme.DarkText
import com.krishnajewellers.app.ui.theme.DeepGold
import com.krishnajewellers.app.ui.theme.GoldAccent
import com.krishnajewellers.app.ui.theme.MutedText
import com.krishnajewellers.app.ui.theme.PrimaryBg
import com.krishnajewellers.app.ui.theme.PremiumGold
import com.krishnajewellers.app.ui.theme.SecondaryBg
import com.krishnajewellers.app.utils.DrawableUtils
import com.krishnajewellers.app.viewmodel.ProductDetailViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    viewModel: ProductDetailViewModel,
    onBackClick: () -> Unit,
    onNavigateToEnquiryChat: (String) -> Unit
) {
    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    val product by viewModel.product.collectAsState()
    val rates by viewModel.rates.collectAsState()
    val selectedWeightOption by viewModel.selectedWeightOption.collectAsState()
    val priceBreakdown by viewModel.currentPriceBreakdown.collectAsState()
    val savedIds by viewModel.savedProductIds.collectAsState()
    val likedIds by viewModel.likedProductIds.collectAsState()
    val storeInfo = viewModel.storeInfo
    val context = LocalContext.current

    val currentProduct = product
    if (currentProduct == null) {
        Box(
            modifier = Modifier.fillMaxSize().background(PrimaryBg),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading jewellery details...")
        }
        return
    }

    val isLiked = likedIds.contains(currentProduct.id)
    val isSaved = savedIds.contains(currentProduct.id)

    // Gallery images: combine primary with gallery list
    val galleryImages = remember(currentProduct) {
        val list = mutableListOf(currentProduct.imageResName)
        list.addAll(currentProduct.galleryImageNames.filter { it != currentProduct.imageResName })
        list
    }
    val pagerState = rememberPagerState(pageCount = { galleryImages.size })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBg)
            .statusBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 110.dp)
        ) {
            // Header Bar with Back and Actions
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
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
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = DarkText
                        )
                    }

                    Row {
                        IconButton(
                            onClick = { viewModel.toggleLiked() },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(CardBg)
                                .border(1.dp, BorderGold, CircleShape)
                        ) {
                            Icon(
                                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (isLiked) Color(0xFFE53935) else DarkText
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = { viewModel.toggleSaved() },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(CardBg)
                                .border(1.dp, BorderGold, CircleShape)
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Save",
                                tint = if (isSaved) DeepGold else DarkText
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "Look at this ${currentProduct.name} at Krishna Jewellers Surat! Estimated price: ${priceBreakdown?.let { MetalRates.formatInr(it.totalEstimatedPrice.toDouble()) } ?: ""}")
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share Jewellery"))
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(CardBg)
                                .border(1.dp, BorderGold, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = DarkText
                            )
                        }
                    }
                }
            }

            // Swipeable Image Gallery
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(310.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xFF141210))
                            .border(1.dp, BorderGold, RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            val imgResName = galleryImages[page]
                            val resId = DrawableUtils.getDrawableResId(context, imgResName)
                            Image(
                                painter = painterResource(id = resId),
                                contentDescription = currentProduct.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Gallery Dots
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        galleryImages.indices.forEach { index ->
                            val isSelected = pagerState.currentPage == index
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp)
                                    .height(5.dp)
                                    .width(if (isSelected) 18.dp else 5.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) DeepGold else GoldAccent)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Product Title and Specifications
            item {
                Column(modifier = Modifier.padding(horizontal = 18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentProduct.category.uppercase(),
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 1.2.sp,
                            color = PremiumGold
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = DeepGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "BIS Hallmarked",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DeepGold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = currentProduct.name,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = DarkText,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Purity & Weight Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SpecChip(label = "Purity", value = currentProduct.purity)
                        SpecChip(label = "Weight Range", value = currentProduct.displayWeightRange)
                        SpecChip(label = "Metal", value = currentProduct.metalType.name)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Product Description
                    Text(
                        text = "DESIGN HIGHLIGHTS",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp,
                        color = DeepGold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = currentProduct.description,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 13.5.sp,
                        color = DarkText.copy(alpha = 0.85f),
                        lineHeight = 21.sp
                    )
                }
                Spacer(modifier = Modifier.height(22.dp))
            }

            // Interactive Price Breakdown Card
            item {
                if (priceBreakdown != null) {
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        PriceBreakdownCard(
                            product = currentProduct,
                            breakdown = priceBreakdown!!,
                            selectedOption = selectedWeightOption,
                            onWeightOptionSelected = { viewModel.selectWeightOption(it) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Contact Store Actions (Call & WhatsApp)
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "CONTACT SURAT SHOWROOM",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp,
                        color = DeepGold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Call Store Button
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .border(1.2.dp, BorderGold, RoundedCornerShape(14.dp))
                                .background(CardBg)
                                .clickable {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${storeInfo.phone}"))
                                    context.startActivity(dialIntent)
                                }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = DeepGold, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Call Store",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DarkText
                                )
                            }
                        }

                        // WhatsApp Store Button
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF25D366))
                                .clickable {
                                    val message = "Namaste Krishna Jewellers, I am interested in viewing ${currentProduct.name} at your Surat showroom."
                                    val waIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=${storeInfo.whatsappNumber}&text=${Uri.encode(message)}"))
                                    context.startActivity(waIntent)
                                }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "WhatsApp",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        // Sticky Bottom Enquire Button Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(CardBg)
                .border(width = 0.8.dp, color = BorderGold.copy(alpha = 0.8f))
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Estimated Price",
                        fontSize = 10.5.sp,
                        color = MutedText
                    )
                    Text(
                        text = priceBreakdown?.let { MetalRates.formatInr(it.totalEstimatedPrice.toDouble()) } ?: "",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = DarkText
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Direct WhatsApp Action
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF25D366))
                            .clickable {
                                val priceStr = priceBreakdown?.let { MetalRates.formatInr(it.totalEstimatedPrice.toDouble()) } ?: ""
                                val msg = "Namaste Krishna Jewellers! I am interested in ${currentProduct.name} (${currentProduct.purity}) priced around $priceStr. Please share availability."
                                val waIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/919876543210?text=" + Uri.encode(msg)))
                                context.startActivity(waIntent)
                            }
                            .padding(horizontal = 12.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "WhatsApp",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }

                    // In-App Chat Action
                    Box(modifier = Modifier.width(135.dp)) {
                        PremiumGoldButton(
                            text = "CHAT",
                            onClick = {
                                val convId = viewModel.startEnquiry()
                                if (convId != null) {
                                    onNavigateToEnquiryChat(convId)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecChip(
    label: String,
    value: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(SecondaryBg)
            .border(0.8.dp, BorderGold, RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Column {
            Text(
                text = label,
                fontSize = 9.5.sp,
                color = MutedText
            )
            Text(
                text = value,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )
        }
    }
}
