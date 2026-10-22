package com.krishnajewellers.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishnajewellers.app.data.model.MetalRates
import com.krishnajewellers.app.data.model.PriceBreakdown
import com.krishnajewellers.app.data.model.Product
import com.krishnajewellers.app.data.model.StoreInfo
import com.krishnajewellers.app.data.model.WeightOption
import com.krishnajewellers.app.data.repository.JewelleryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ProductDetailViewModel(
    private val repository: JewelleryRepository
) : ViewModel() {

    val rates: StateFlow<MetalRates> = repository.rates
    val savedProductIds: StateFlow<Set<String>> = repository.savedProductIds
    val likedProductIds: StateFlow<Set<String>> = repository.likedProductIds
    val storeInfo: StoreInfo = repository.storeInfo

    private val _productId = MutableStateFlow<String?>(null)

    private val _selectedWeightOption = MutableStateFlow(WeightOption.MID)
    val selectedWeightOption: StateFlow<WeightOption> = _selectedWeightOption.asStateFlow()

    val product: StateFlow<Product?> = combine(_productId, repository.products) { id, list ->
        if (id == null) null else list.find { it.id == id }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentPriceBreakdown: StateFlow<PriceBreakdown?> = combine(
        product,
        repository.rates,
        _selectedWeightOption
    ) { prod, currentRates, weightOpt ->
        prod?.calculatePrice(currentRates, weightOpt)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun loadProduct(id: String) {
        _productId.value = id
        _selectedWeightOption.value = WeightOption.MID
    }

    fun selectWeightOption(option: WeightOption) {
        _selectedWeightOption.value = option
    }

    fun toggleSaved() {
        _productId.value?.let { repository.toggleSaved(it) }
    }

    fun toggleLiked() {
        _productId.value?.let { repository.toggleLiked(it) }
    }

    fun startEnquiry(): String? {
        val prod = product.value ?: return null
        return repository.startProductEnquiry(prod)
    }
}
