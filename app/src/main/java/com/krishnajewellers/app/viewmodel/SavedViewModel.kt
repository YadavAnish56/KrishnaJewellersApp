package com.krishnajewellers.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishnajewellers.app.data.model.MetalRates
import com.krishnajewellers.app.data.model.Product
import com.krishnajewellers.app.data.repository.JewelleryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SavedViewModel(
    private val repository: JewelleryRepository
) : ViewModel() {

    val rates: StateFlow<MetalRates> = repository.rates
    val savedProductIds: StateFlow<Set<String>> = repository.savedProductIds
    val likedProductIds: StateFlow<Set<String>> = repository.likedProductIds

    private val _isGridView = MutableStateFlow(true)
    val isGridView: StateFlow<Boolean> = _isGridView.asStateFlow()

    val savedProducts: StateFlow<List<Product>> = combine(
        repository.products,
        repository.savedProductIds
    ) { products, savedIds ->
        products.filter { savedIds.contains(it.id) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleLayout() {
        _isGridView.value = !_isGridView.value
    }

    fun removeSaved(productId: String) {
        repository.toggleSaved(productId)
    }

    fun toggleLiked(productId: String) {
        repository.toggleLiked(productId)
    }

    fun startEnquiry(product: Product): String {
        return repository.startProductEnquiry(product)
    }
}
