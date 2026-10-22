package com.krishnajewellers.app.data.model

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

data class MetalRates(
    val id: Int = 1,
    val gold24kPer10g: Double = 73500.0,
    val gold22kPer10g: Double = 67375.0,
    val gold18kPer10g: Double = 55125.0,
    val silverPerKg: Double = 89000.0,
    val lastUpdated: String = "Today, 10:30 AM",
    val updatedBy: String = "Surat Showroom Admin",
    val isSample: Boolean = true
) {
    fun getRatePerGram(purity: String): Double {
        return when {
            purity.contains("24K", ignoreCase = true) -> gold24kPer10g / 10.0
            purity.contains("22K", ignoreCase = true) || purity.contains("916", ignoreCase = true) -> gold22kPer10g / 10.0
            purity.contains("18K", ignoreCase = true) || purity.contains("750", ignoreCase = true) -> gold18kPer10g / 10.0
            purity.contains("Silver", ignoreCase = true) || purity.contains("92.5", ignoreCase = true) -> silverPerKg / 1000.0
            else -> gold22kPer10g / 10.0
        }
    }

    val gold24kPerGram: Double get() = gold24kPer10g / 10.0
    val gold22kPerGram: Double get() = gold22kPer10g / 10.0
    val gold18kPerGram: Double get() = gold18kPer10g / 10.0
    val silverPerGram: Double get() = silverPerKg / 1000.0

    companion object {
        fun formatInr(amount: Double): String {
            val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
            format.maximumFractionDigits = 0
            return format.format(amount)
        }

        fun formatInrDecimal(amount: BigDecimal): String {
            val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
            format.maximumFractionDigits = 2
            format.minimumFractionDigits = 2
            return format.format(amount)
        }
    }
}
