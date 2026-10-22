package com.krishnajewellers.app.data.model

enum class MetalType {
    GOLD,
    SILVER
}

enum class MakingChargeType {
    PERCENTAGE,
    PER_GRAM,
    FIXED_AMOUNT
}

enum class WeightOption {
    MIN,
    MID,
    MAX
}

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val metalType: MetalType,
    val purity: String,
    val description: String,
    val minWeight: Double,
    val midWeight: Double,
    val maxWeight: Double,
    val makingChargeType: MakingChargeType = MakingChargeType.PERCENTAGE,
    val makingChargeValue: Double = 12.0,
    val imageResName: String,
    val galleryImageNames: List<String> = emptyList(),
    val isTrending: Boolean = false,
    val isMostLoved: Boolean = false,
    val isNewArrival: Boolean = false,
    val isVisible: Boolean = true,
    val isDemoCreated: Boolean = false
) {
    fun getWeightForOption(option: WeightOption): Double = when (option) {
        WeightOption.MIN -> minWeight
        WeightOption.MID -> midWeight
        WeightOption.MAX -> maxWeight
    }

    fun calculatePrice(rates: MetalRates, option: WeightOption = WeightOption.MID): PriceBreakdown {
        val weight = getWeightForOption(option)
        val ratePerGram = rates.getRatePerGram(purity)
        return PriceBreakdown.calculate(
            weight = weight,
            ratePerGram = ratePerGram,
            chargeType = makingChargeType,
            chargeValue = makingChargeValue
        )
    }

    val displayWeightRange: String get() = "${minWeight}g - ${maxWeight}g"
}
