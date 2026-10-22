package com.krishnajewellers.app.data.model

import java.math.BigDecimal
import java.math.RoundingMode

data class PriceBreakdown(
    val weightGrams: Double,
    val metalRatePerGram: BigDecimal,
    val metalValue: BigDecimal,
    val makingCharge: BigDecimal,
    val makingChargeDescription: String,
    val subtotal: BigDecimal,
    val gstRate: BigDecimal = BigDecimal("0.03"),
    val gstAmount: BigDecimal,
    val totalEstimatedPrice: BigDecimal
) {
    companion object {
        fun calculate(
            weight: Double,
            ratePerGram: Double,
            chargeType: MakingChargeType,
            chargeValue: Double
        ): PriceBreakdown {
            val weightBd = BigDecimal.valueOf(weight).setScale(3, RoundingMode.HALF_UP)
            val rateBd = BigDecimal.valueOf(ratePerGram).setScale(2, RoundingMode.HALF_UP)
            val metalVal = (weightBd * rateBd).setScale(2, RoundingMode.HALF_UP)

            val (makingChargeVal, chargeDesc) = when (chargeType) {
                MakingChargeType.PERCENTAGE -> {
                    val charge = (metalVal * BigDecimal.valueOf(chargeValue) / BigDecimal(100))
                        .setScale(2, RoundingMode.HALF_UP)
                    Pair(charge, "$chargeValue% of metal value")
                }
                MakingChargeType.PER_GRAM -> {
                    val charge = (weightBd * BigDecimal.valueOf(chargeValue))
                        .setScale(2, RoundingMode.HALF_UP)
                    Pair(charge, "₹${chargeValue.toInt()}/gram")
                }
                MakingChargeType.FIXED_AMOUNT -> {
                    val charge = BigDecimal.valueOf(chargeValue)
                        .setScale(2, RoundingMode.HALF_UP)
                    Pair(charge, "Flat ₹${chargeValue.toInt()}")
                }
            }

            val subtotalVal = (metalVal + makingChargeVal).setScale(2, RoundingMode.HALF_UP)
            val gstVal = (subtotalVal * BigDecimal("0.03")).setScale(2, RoundingMode.HALF_UP)
            val totalVal = (subtotalVal + gstVal).setScale(2, RoundingMode.HALF_UP)

            return PriceBreakdown(
                weightGrams = weight,
                metalRatePerGram = rateBd,
                metalValue = metalVal,
                makingCharge = makingChargeVal,
                makingChargeDescription = chargeDesc,
                subtotal = subtotalVal,
                gstRate = BigDecimal("0.03"),
                gstAmount = gstVal,
                totalEstimatedPrice = totalVal
            )
        }
    }
}
