package com.krishnajewellers.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishnajewellers.app.data.model.JewelleryCategories
import com.krishnajewellers.app.data.model.MetalRates
import com.krishnajewellers.app.data.model.MetalType
import com.krishnajewellers.app.data.model.Product
import com.krishnajewellers.app.data.repository.JewelleryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

enum class SortOrder {
    POPULAR,
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW,
    WEIGHT_LOW_TO_HIGH
}

class ExploreViewModel(
    private val repository: JewelleryRepository
) : ViewModel() {

    val rates: StateFlow<MetalRates> = repository.rates
    val savedProductIds: StateFlow<Set<String>> = repository.savedProductIds
    val likedProductIds: StateFlow<Set<String>> = repository.likedProductIds

    private val _selectedMetal = MutableStateFlow(MetalType.GOLD)
    val selectedMetal: StateFlow<MetalType> = _selectedMetal.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All Gold")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.POPULAR)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    val categories: StateFlow<List<String>> = _selectedMetal.map { metal ->
        if (metal == MetalType.GOLD) {
            JewelleryCategories.goldCategories
        } else {
            JewelleryCategories.silverCategories
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), JewelleryCategories.goldCategories)

    val filteredProducts: StateFlow<List<Product>> = combine(
        combine(repository.products, repository.rates, _selectedMetal) { p, r, m ->
            Triple(p, r, m)
        },
        combine(_selectedCategory, _searchQuery, _sortOrder) { c, q, s ->
            Triple(c, q, s)
        }
    ) { (products, currentRates, metal), (cat, query, sort) ->
        var list = products.filter { it.isVisible && it.metalType == metal }

        if (cat != "All Gold" && cat != "All Silver") {
            list = list.filter { it.category.equals(cat, ignoreCase = true) }
        }

        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.name.lowercase().contains(q) ||
                        it.category.lowercase().contains(q) ||
                        it.description.lowercase().contains(q) ||
                        it.purity.lowercase().contains(q)
            }
        }

        when (sort) {
            SortOrder.POPULAR -> list.sortedByDescending { if (it.isMostLoved) 2 else if (it.isTrending) 1 else 0 }
            SortOrder.PRICE_LOW_TO_HIGH -> list.sortedBy { it.calculatePrice(currentRates).totalEstimatedPrice }
            SortOrder.PRICE_HIGH_TO_LOW -> list.sortedByDescending { it.calculatePrice(currentRates).totalEstimatedPrice }
            SortOrder.WEIGHT_LOW_TO_HIGH -> list.sortedBy { it.midWeight }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectMetal(metal: MetalType) {
        _selectedMetal.value = metal
        _selectedCategory.value = if (metal == MetalType.GOLD) "All Gold" else "All Silver"
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun setSortOrder(order: SortOrder) {
        _sortOrder.value = order
    }

    fun toggleSaved(productId: String) {
        repository.toggleSaved(productId)
    }

    fun toggleLiked(productId: String) {
        repository.toggleLiked(productId)
    }
}
