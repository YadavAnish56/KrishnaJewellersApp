package com.krishnajewellers.app.data.model

data class CustomDesignRequest(
    val id: String,
    val customerName: String,
    val customerPhone: String,
    val title: String,
    val description: String,
    val preferredWeight: Double,
    val budget: Double,
    val occasion: String,
    val attachments: List<String> = emptyList(),
    val videoReference: String? = null,
    val status: String = "Pending", // Pending, Quoted, Accepted, Declined
    val quotedPrice: Double? = null,
    val quoteNote: String? = null,
    val date: String
)
