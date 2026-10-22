package com.krishnajewellers.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishnajewellers.app.data.model.CustomDesignRequest
import com.krishnajewellers.app.data.repository.JewelleryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class CustomRequestViewModel(
    private val repository: JewelleryRepository
) : ViewModel() {

    val customRequests: StateFlow<List<CustomDesignRequest>> = repository.customRequests

    val occasions = listOf(
        "Bridal / Wedding",
        "Engagement Ceremony",
        "Anniversary Celebration",
        "Auspicious Festival / Diwali",
        "Baby Naming / Mundan",
        "Daily Luxury Wear",
        "Heritage Heirloom Gift"
    )

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _preferredWeight = MutableStateFlow("25.0")
    val preferredWeight: StateFlow<String> = _preferredWeight.asStateFlow()

    private val _budget = MutableStateFlow("180000")
    val budget: StateFlow<String> = _budget.asStateFlow()

    private val _selectedOccasion = MutableStateFlow(occasions[0])
    val selectedOccasion: StateFlow<String> = _selectedOccasion.asStateFlow()

    private val _attachments = MutableStateFlow<List<String>>(emptyList())
    val attachments: StateFlow<List<String>> = _attachments.asStateFlow()

    private val _submissionSuccess = MutableStateFlow(false)
    val submissionSuccess: StateFlow<Boolean> = _submissionSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun onTitleChanged(v: String) { _title.value = v }
    fun onDescChanged(v: String) { _description.value = v }
    fun onWeightChanged(v: String) { _preferredWeight.value = v }
    fun onBudgetChanged(v: String) { _budget.value = v }
    fun onOccasionSelected(v: String) { _selectedOccasion.value = v }

    fun addAttachmentUri(uriStr: String) {
        if (_attachments.value.size < 5) {
            _attachments.value = _attachments.value + uriStr
        }
    }

    fun removeAttachment(index: Int) {
        if (index in _attachments.value.indices) {
            val list = _attachments.value.toMutableList()
            list.removeAt(index)
            _attachments.value = list
        }
    }

    fun submitRequest(customerName: String, customerPhone: String) {
        if (_title.value.isBlank()) {
            _errorMessage.value = "Please enter a design title"
            return
        }
        if (_description.value.isBlank()) {
            _errorMessage.value = "Please describe your custom design vision"
            return
        }
        val weight = _preferredWeight.value.toDoubleOrNull()
        if (weight == null || weight <= 0) {
            _errorMessage.value = "Please enter a valid preferred weight in grams"
            return
        }
        val budgetVal = _budget.value.toDoubleOrNull()
        if (budgetVal == null || budgetVal <= 0) {
            _errorMessage.value = "Please enter an approximate budget in INR"
            return
        }

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val dateStr = dateFormat.format(Date())
        val reqId = "CR-" + (2000..9999).random()

        val req = CustomDesignRequest(
            id = reqId,
            customerName = customerName.ifBlank { "Valued Customer" },
            customerPhone = customerPhone.ifBlank { "+91 98251 98765" },
            title = _title.value.trim(),
            description = _description.value.trim(),
            preferredWeight = weight,
            budget = budgetVal,
            occasion = _selectedOccasion.value,
            attachments = _attachments.value,
            status = "Pending",
            date = dateStr
        )

        repository.submitCustomRequest(req)
        _submissionSuccess.value = true
        _errorMessage.value = null

        // Reset fields
        _title.value = ""
        _description.value = ""
        _preferredWeight.value = "25.0"
        _budget.value = "180000"
        _attachments.value = emptyList()
    }

    fun resetSubmissionStatus() {
        _submissionSuccess.value = false
        _errorMessage.value = null
    }
}
