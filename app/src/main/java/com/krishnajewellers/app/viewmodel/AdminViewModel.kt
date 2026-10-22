package com.krishnajewellers.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishnajewellers.app.data.model.CustomDesignRequest
import com.krishnajewellers.app.data.model.EnquiryConversation
import com.krishnajewellers.app.data.model.MakingChargeType
import com.krishnajewellers.app.data.model.MetalRates
import com.krishnajewellers.app.data.model.MetalType
import com.krishnajewellers.app.data.model.Product
import com.krishnajewellers.app.data.repository.JewelleryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.UUID

data class AdminDashboardStats(
    val totalProducts: Int,
    val totalEnquiries: Int,
    val totalCustomRequests: Int,
    val pendingQuotesCount: Int
)

class AdminViewModel(
    private val repository: JewelleryRepository
) : ViewModel() {

    val rates: StateFlow<MetalRates> = repository.rates
    val products: StateFlow<List<Product>> = repository.products
    val customRequests: StateFlow<List<CustomDesignRequest>> = repository.customRequests
    val conversations: StateFlow<List<EnquiryConversation>> = repository.conversations

    val stats: StateFlow<AdminDashboardStats> = combine(
        products,
        conversations,
        customRequests
    ) { prods, convs, reqs ->
        AdminDashboardStats(
            totalProducts = prods.size,
            totalEnquiries = convs.size,
            totalCustomRequests = reqs.size,
            pendingQuotesCount = reqs.count { it.status == "Pending" }
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AdminDashboardStats(0, 0, 0, 0)
    )

    // Rate editing state
    private val _gold24kInput = MutableStateFlow("73500")
    val gold24kInput: StateFlow<String> = _gold24kInput.asStateFlow()

    private val _gold22kInput = MutableStateFlow("67375")
    val gold22kInput: StateFlow<String> = _gold22kInput.asStateFlow()

    private val _gold18kInput = MutableStateFlow("55125")
    val gold18kInput: StateFlow<String> = _gold18kInput.asStateFlow()

    private val _silverKgInput = MutableStateFlow("89000")
    val silverKgInput: StateFlow<String> = _silverKgInput.asStateFlow()

    private val _rateUpdateFeedback = MutableStateFlow<String?>(null)
    val rateUpdateFeedback: StateFlow<String?> = _rateUpdateFeedback.asStateFlow()

    init {
        // Sync rate input fields with current rates
        val current = rates.value
        _gold24kInput.value = current.gold24kPer10g.toInt().toString()
        _gold22kInput.value = current.gold22kPer10g.toInt().toString()
        _gold18kInput.value = current.gold18kPer10g.toInt().toString()
        _silverKgInput.value = current.silverPerKg.toInt().toString()
    }

    fun onGold24kChanged(v: String) { _gold24kInput.value = v }
    fun onGold22kChanged(v: String) { _gold22kInput.value = v }
    fun onGold18kChanged(v: String) { _gold18kInput.value = v }
    fun onSilverKgChanged(v: String) { _silverKgInput.value = v }

    fun saveRates() {
        val g24 = _gold24kInput.value.toDoubleOrNull() ?: 73500.0
        val g22 = _gold22kInput.value.toDoubleOrNull() ?: 67375.0
        val g18 = _gold18kInput.value.toDoubleOrNull() ?: 55125.0
        val sil = _silverKgInput.value.toDoubleOrNull() ?: 89000.0

        repository.updateRates(g24, g22, g18, sil, "Owner Admin (Surat)")
        _rateUpdateFeedback.value = "Precious metal rates updated successfully! Customer catalog and calculators updated."
    }

    fun clearFeedback() {
        _rateUpdateFeedback.value = null
    }

    // Product Management
    fun addProduct(
        name: String,
        category: String,
        metalType: MetalType,
        purity: String,
        description: String,
        minWeight: Double,
        midWeight: Double,
        maxWeight: Double,
        chargeType: MakingChargeType,
        chargeValue: Double,
        imageRes: String
    ) {
        val id = "prod-" + UUID.randomUUID().toString().take(8)
        val product = Product(
            id = id,
            name = name,
            category = category,
            metalType = metalType,
            purity = purity,
            description = description,
            minWeight = minWeight,
            midWeight = midWeight,
            maxWeight = maxWeight,
            makingChargeType = chargeType,
            makingChargeValue = chargeValue,
            imageResName = imageRes,
            galleryImageNames = listOf(imageRes),
            isTrending = true,
            isMostLoved = false,
            isNewArrival = true,
            isVisible = true,
            isDemoCreated = true
        )
        repository.addProduct(product)
    }

    fun toggleProductVisibility(id: String, visible: Boolean) {
        repository.toggleProductVisibility(id, visible)
    }

    fun deleteProduct(id: String) {
        repository.deleteProduct(id)
    }

    // Custom Request Quote
    fun quoteCustomRequest(id: String, status: String, price: Double, note: String) {
        repository.updateCustomRequestQuote(id, status, price, note)
    }
}
