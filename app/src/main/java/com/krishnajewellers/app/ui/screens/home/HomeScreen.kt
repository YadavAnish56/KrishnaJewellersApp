package com.krishnajewellers.app.ui.screens.home

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.krishnajewellers.app.R
import com.krishnajewellers.app.data.model.MetalType
import com.krishnajewellers.app.data.model.Product
import com.krishnajewellers.app.ui.components.BannerCarousel
import com.krishnajewellers.app.ui.components.GoldRateCard
import com.krishnajewellers.app.ui.components.JewelleryProductCard
import com.krishnajewellers.app.ui.components.LuxuryTopBar
import com.krishnajewellers.app.ui.theme.BorderGold
import com.krishnajewellers.app.ui.theme.CardBg
import com.krishnajewellers.app.ui.theme.DarkText
import com.krishnajewellers.app.ui.theme.DeepGold
import com.krishnajewellers.app.ui.theme.GoldAccent
import com.krishnajewellers.app.ui.theme.GoldGradient
import com.krishnajewellers.app.ui.theme.MutedText
import com.krishnajewellers.app.ui.theme.PrimaryBg
import com.krishnajewellers.app.ui.theme.PremiumGold
import com.krishnajewellers.app.ui.theme.SecondaryBg
import com.krishnajewellers.app.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToProduct: (String) -> Unit,
    onNavigateToExplore: (MetalType) -> Unit,
    onNavigateToCustomRequest: () -> Unit,
    onNavigateToSaved: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToEnquiryChat: (String) -> Unit
) {
    val rates by viewModel.rates.collectAsState()
    val mostLoved by viewModel.mostLovedProducts.collectAsState()
    val trending by viewModel.trendingProducts.collectAsState()
    val savedIds by viewModel.savedProductIds.collectAsState()
    val likedIds by viewModel.likedProductIds.collectAsState()
    val storeInfo = viewModel.storeInfo
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBg)
    ) {
        // Sticky Luxury Top Header
        LuxuryTopBar(
            savedCount = savedIds.size,
            onSearchClick = { onNavigateToExplore(MetalType.GOLD) },
            onSavedClick = onNavigateToSaved,
            onProfileClick = onNavigateToProfile
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // 1. Hero Promotional Banner Carousel
            item {
                Spacer(modifier = Modifier.height(6.dp))
                BannerCarousel(
                    banners = viewModel.banners,
                    onBannerClick = { banner ->
                        onNavigateToExplore(
                            if (banner.targetCategory == "Payal") MetalType.SILVER else MetalType.GOLD
                        )
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            // 2. Today's Precious Metal Rates Card
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    GoldRateCard(rates = rates)
                }
                Spacer(modifier = Modifier.height(26.dp))
            }

            // 3. Featured Collections: Gold Collection & Silver Collection
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "EXPLORE COLLECTIONS",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 1.2.sp,
                        color = DeepGold
                    )
                    Text(
                        text = "Curated jewellery ensembles by master craftsmen",
                        fontSize = 11.5.sp,
                        color = MutedText
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Gold Collection Card
                        CollectionCard(
                            modifier = Modifier.weight(1f),
                            title = "GOLD COLLECTION",
                            subtitle = "22K & 18K Hallmarked",
                            imageRes = R.drawable.real_bridal_choker,
                            tagColor = PremiumGold,
                            onClick = { onNavigateToExplore(MetalType.GOLD) }
                        )

                        // Silver Collection Card
                        CollectionCard(
                            modifier = Modifier.weight(1f),
                            title = "SILVER COLLECTION",
                            subtitle = "92.5 Sterling Silver",
                            imageRes = R.drawable.real_silver_payal,
                            tagColor = Color(0xFF6B7280),
                            onClick = { onNavigateToExplore(MetalType.SILVER) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
            }

            // 4. Trending Jewellery (Horizontal Reel)
            item {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TRENDING NOW",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                letterSpacing = 1.2.sp,
                                color = DeepGold
                            )
                            Text(
                                text = "Most viewed pieces this festive season",
                                fontSize = 11.5.sp,
                                color = MutedText
                            )
                        }

                        Text(
                            text = "View All",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DeepGold,
                            modifier = Modifier.clickable { onNavigateToExplore(MetalType.GOLD) }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(trending, key = { it.id }) { prod ->
                            Box(modifier = Modifier.width(220.dp)) {
                                JewelleryProductCard(
                                    product = prod,
                                    rates = rates,
                                    isLiked = likedIds.contains(prod.id),
                                    isSaved = savedIds.contains(prod.id),
                                    onProductClick = { onNavigateToProduct(prod.id) },
                                    onLikeClick = { viewModel.toggleLiked(prod.id) },
                                    onSaveClick = { viewModel.toggleSaved(prod.id) },
                                    onEnquireClick = {
                                        val convId = viewModel.startEnquiry(prod)
                                        onNavigateToEnquiryChat(convId)
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
            }

            // 5. Custom Jewellery Banner ("Your Vision, Our Craft")
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(GoldGradient)
                        .clickable { onNavigateToCustomRequest() }
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "BESPOKE ARTISTRY",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 1.5.sp,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Your Vision, Our Craft",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Have a dream bridal necklace or gold kada in mind? Upload reference sketches and our Surat karigars will create an authentic 3D quote for you.",
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = "Request Custom Gold Design",
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp,
                                color = DeepGold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = DeepGold,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
            }

            // 6. Most Loved Designs (Vertical Feed)
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "MOST LOVED DESIGNS",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 1.2.sp,
                        color = DeepGold
                    )
                    Text(
                        text = "Handcrafted masterpieces cherished by Surat patrons",
                        fontSize = 11.5.sp,
                        color = MutedText
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

            items(mostLoved, key = { it.id }) { prod ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    JewelleryProductCard(
                        product = prod,
                        rates = rates,
                        isLiked = likedIds.contains(prod.id),
                        isSaved = savedIds.contains(prod.id),
                        onProductClick = { onNavigateToProduct(prod.id) },
                        onLikeClick = { viewModel.toggleLiked(prod.id) },
                        onSaveClick = { viewModel.toggleSaved(prod.id) },
                        onEnquireClick = {
                            val convId = viewModel.startEnquiry(prod)
                            onNavigateToEnquiryChat(convId)
                        }
                    )
                }
            }

            // 7. Store Information Preview Card
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderGold, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SecondaryBg)
                    ) {
                        Column {
                            // Showroom Real Photograph Banner
                            Image(
                                painter = painterResource(id = R.drawable.showroom_surat_interior),
                                contentDescription = "Surat Flagship Showroom",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(170.dp),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )

                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = DeepGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "VISIT OUR SURAT SHOWROOM",
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.5.sp,
                                        letterSpacing = 1.sp,
                                        color = DeepGold
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = storeInfo.name,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = DarkText
                                )

                                Text(
                                    text = storeInfo.fullAddress,
                                    fontSize = 12.sp,
                                    color = MutedText,
                                    modifier = Modifier.padding(top = 2.dp),
                                    lineHeight = 17.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "Timings: ${storeInfo.timing}",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = DarkText
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Google Maps Action
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(DeepGold)
                                            .clickable {
                                                val uri = Uri.parse("geo:0,0?q=" + Uri.encode(storeInfo.mapQuery))
                                                val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                                                context.startActivity(mapIntent)
                                            }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Directions",
                                            fontFamily = FontFamily.SansSerif,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 11.5.sp,
                                            color = Color.White
                                        )
                                    }

                                    // Call Action
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .border(1.dp, DeepGold, RoundedCornerShape(12.dp))
                                            .background(CardBg)
                                            .clickable {
                                                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${storeInfo.phone}"))
                                                context.startActivity(dialIntent)
                                            }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Call Store",
                                            fontFamily = FontFamily.SansSerif,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 11.5.sp,
                                            color = DeepGold
                                        )
                                    }

                                    // WhatsApp Action
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFF25D366))
                                            .clickable {
                                                val msg = "Hello Krishna Jewellers Surat! I would like to visit your showroom on Godadara Road."
                                                val waIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/919876543210?text=" + Uri.encode(msg)))
                                                context.startActivity(waIntent)
                                            }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "WhatsApp",
                                            fontFamily = FontFamily.SansSerif,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 11.5.sp,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CollectionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    imageRes: Int,
    tagColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, BorderGold, RoundedCornerShape(18.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF141210))
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = DarkText,
                letterSpacing = 0.5.sp
            )

            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = tagColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
