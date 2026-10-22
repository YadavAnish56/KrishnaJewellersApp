package com.krishnajewellers.app.data.model

data class EnquiryMessage(
    val id: String,
    val conversationId: String,
    val sender: String, // "customer" or "shop"
    val message: String,
    val timestamp: Long,
    val attachedProductId: String? = null,
    val attachedProductName: String? = null,
    val attachedProductCategory: String? = null,
    val attachedProductImage: String? = null,
    val attachedProductPrice: Double? = null
)

data class EnquiryConversation(
    val id: String,
    val customerName: String = "Valued Customer",
    val customerPhone: String = "+91 98250 12345",
    val lastMessage: String,
    val lastTimestamp: Long,
    val unreadCount: Int = 0,
    val productId: String? = null,
    val productName: String? = null,
    val productImage: String? = null
)
