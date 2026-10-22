package com.krishnajewellers.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishnajewellers.app.data.model.MetalRates
import com.krishnajewellers.app.data.model.Product
import com.krishnajewellers.app.data.model.StoreInfo
import com.krishnajewellers.app.data.repository.JewelleryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class BannerItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val buttonText: String,
    val imageResName: String,
    val targetCategory: String
)

class HomeViewModel(
    private val repository: JewelleryRepository
) : ViewModel() {

    val rates: StateFlow<MetalRates> = repository.rates

    val savedProductIds: StateFlow<Set<String>> = repository.savedProductIds

    val likedProductIds: StateFlow<Set<String>> = repository.likedProductIds

    val storeInfo: StoreInfo = repository.storeInfo

    val banners = listOf(
        BannerItem(
            id = "b1",
            title = "The Royal Vivah Collection 2026",
            subtitle = "Exquisite 22K Hallmarked Gold & Polki Bridal Masterpieces",
            buttonText = "Explore Bridal",
            imageResName = "banner_bridal_vivah",
            targetCategory = "Necklaces"
        ),
        BannerItem(
            id = "b2",
            title = "Zero Making Charge on Bullion",
            subtitle = "Certified 24K 999.9 Fine Gold Coins & Swiss Investment Bars",
            buttonText = "View Bullion",
            imageResName = "real_gold_bullion",
            targetCategory = "Coins"
        ),
        BannerItem(
            id = "b3",
            title = "Handcrafted Gujarat Pachheli",
            subtitle = "Royal Peacock Filigree Kadas with Rubies & Emeralds",
            buttonText = "Explore Bangles",
            imageResName = "real_gold_bangles",
            targetCategory = "Bangles"
        ),
        BannerItem(
            id = "b4",
            title = "Pure 925 Sterling Silver Payal",
            subtitle = "Surat Melodious Chham Chham Bridal Anklets with Ghungroos",
            buttonText = "Explore Silver",
            imageResName = "real_silver_payal",
            targetCategory = "Payal"
        )
    )

    val mostLovedProducts: StateFlow<List<Product>> = repository.products.combine(repository.rates) { list, _ ->
        list.filter { it.isMostLoved && it.isVisible }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trendingProducts: StateFlow<List<Product>> = repository.products.combine(repository.rates) { list, _ ->
        list.filter { it.isTrending && it.isVisible }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleSaved(productId: String) {
        repository.toggleSaved(productId)
    }

    fun toggleLiked(productId: String) {
        repository.toggleLiked(productId)
    }

    fun startEnquiry(product: Product): String {
        return repository.startProductEnquiry(product)
    }
}
