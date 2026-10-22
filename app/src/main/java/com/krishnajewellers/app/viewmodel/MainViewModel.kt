package com.krishnajewellers.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishnajewellers.app.data.repository.JewelleryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: JewelleryRepository
) : ViewModel() {

    val isOnboardingCompleted = repository.preferences.isOnboardingCompleted.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val isLoggedIn = repository.preferences.isLoggedIn.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val isGuest = repository.preferences.isGuest.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val userName = repository.preferences.userName.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "Kunal Mehta"
    )

    val userEmail = repository.preferences.userEmail.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "kunal.mehta@example.com"
    )

    val userPhone = repository.preferences.userPhone.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "+91 98251 98765"
    )

    val isAdminMode = repository.preferences.isAdminMode.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val savedCount: StateFlow<Int> = MutableStateFlow(0).apply {
        viewModelScope.launch {
            repository.savedProductIds.collect {
                value = it.size
            }
        }
    }

    val unreadEnquiryCount: StateFlow<Int> = MutableStateFlow(0).apply {
        viewModelScope.launch {
            repository.conversations.collect { convs ->
                value = convs.sumOf { it.unreadCount }
            }
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            repository.preferences.setOnboardingCompleted(true)
        }
    }

    fun loginDemo(name: String, email: String, phone: String) {
        viewModelScope.launch {
            repository.preferences.setLoggedIn(name, email, phone)
        }
    }

    fun continueAsGuest() {
        viewModelScope.launch {
            repository.preferences.setGuestMode()
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.preferences.logout()
        }
    }

    fun toggleAdminMode(enabled: Boolean) {
        viewModelScope.launch {
            repository.preferences.setAdminMode(enabled)
        }
    }
}
