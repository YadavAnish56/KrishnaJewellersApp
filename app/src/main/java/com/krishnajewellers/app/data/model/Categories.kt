package com.krishnajewellers.app.data.model

object JewelleryCategories {
    val goldCategories = listOf(
        "All Gold",
        "Necklaces",
        "Earrings",
        "Rings — Ladies",
        "Rings — Gents",
        "Chains",
        "Bangles",
        "Mangalsutra",
        "Pendants",
        "Bracelets",
        "Kada",
        "Bali",
        "Maang Tikka",
        "Nath",
        "Biscuits",
        "Coins"
    )

    val silverCategories = listOf(
        "All Silver",
        "Payal",
        "Chains",
        "Bangles",
        "Bracelets",
        "Pendants",
        "Rings — Ladies",
        "Rings — Gents",
        "Kada",
        "Kandora",
        "Mangalsutra",
        "Biscuits",
        "Coins"
    )

    fun getCategoryDescription(category: String): String = when (category) {
        "Necklaces" -> "Intricate bridal & royal chokers"
        "Earrings" -> "Jhumkas, studs & chandbalis"
        "Rings — Ladies" -> "Diamond & gold solitaires"
        "Rings — Gents" -> "Bold signet & classic bands"
        "Chains" -> "Classic rope & curb link patterns"
        "Bangles" -> "Traditional kadas & chudas"
        "Mangalsutra" -> "Sacred black beads & gold"
        "Pendants" -> "Deity, floral & modern drops"
        "Payal" -> "Melodious silver anklets"
        "Kada" -> "Substantial solid statement kadas"
        "Bracelets" -> "Flexible tennis & link motifs"
        "Maang Tikka" -> "Regal bridal forehead ornaments"
        "Nath" -> "Heritage Gujarati & Rajasthani nose rings"
        "Biscuits" -> "999 fine bullion investment bars"
        "Coins" -> "Auspicious Laxmi & Ganesh minted coins"
        "Kandora" -> "Ornate silver waist chains"
        else -> "Authentic handcrafted jewellery"
    }
}
