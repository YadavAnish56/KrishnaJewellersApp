package com.krishnajewellers.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krishnajewellers.app.data.model.EnquiryConversation
import com.krishnajewellers.app.data.model.EnquiryMessage
import com.krishnajewellers.app.data.model.Product
import com.krishnajewellers.app.data.repository.JewelleryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ChatViewModel(
    private val repository: JewelleryRepository
) : ViewModel() {

    val conversations: StateFlow<List<EnquiryConversation>> = repository.conversations

    private val _activeConversationId = MutableStateFlow<String?>(null)
    val activeConversationId: StateFlow<String?> = _activeConversationId.asStateFlow()

    private val _messageInput = MutableStateFlow("")
    val messageInput: StateFlow<String> = _messageInput.asStateFlow()

    val activeConversation: StateFlow<EnquiryConversation?> = combine(
        _activeConversationId,
        repository.conversations
    ) { id, list ->
        if (id == null) null else list.find { it.id == id }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val activeMessages: StateFlow<List<EnquiryMessage>> = combine(
        _activeConversationId,
        repository.messages
    ) { id, map ->
        if (id == null) emptyList() else map[id] ?: emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectConversation(id: String) {
        _activeConversationId.value = id
        repository.markConversationRead(id)
    }

    fun onMessageInputChanged(text: String) {
        _messageInput.value = text
    }

    fun sendMessage(asOwner: Boolean = false) {
        val text = _messageInput.value.trim()
        val convId = _activeConversationId.value ?: return
        if (text.isBlank()) return

        val sender = if (asOwner) "shop" else "customer"
        repository.sendMessage(convId, sender, text)
        _messageInput.value = ""
    }

    fun sendQuickReply(replyText: String, asOwner: Boolean = true) {
        val convId = _activeConversationId.value ?: return
        val sender = if (asOwner) "shop" else "customer"
        repository.sendMessage(convId, sender, replyText)
    }
}
