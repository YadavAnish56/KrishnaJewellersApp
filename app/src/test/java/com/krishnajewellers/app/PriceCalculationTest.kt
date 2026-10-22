package com.krishnajewellers.app

import com.krishnajewellers.app.data.model.MakingChargeType
import com.krishnajewellers.app.data.model.MetalRates
import com.krishnajewellers.app.data.model.PriceBreakdown
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

class PriceCalculationTest {

    @Test
    fun testGoldRatePerGramCalculations() {
        val rates = MetalRates(
            gold24kPer10g = 73500.0,
            gold22kPer10g = 67000.0,
            gold18kPer10g = 55000.0,
            silverPerKg = 90000.0
        )

        assertEquals(7350.0, rates.getRatePerGram("24K (999 Purity)"), 0.001)
        assertEquals(6700.0, rates.getRatePerGram("22K (916 Hallmarked)"), 0.001)
        assertEquals(5500.0, rates.getRatePerGram("18K (750 Hallmarked)"), 0.001)
        assertEquals(90.0, rates.getRatePerGram("92.5 Sterling Silver"), 0.001)
    }

    @Test
    fun testPriceBreakdownPercentageCalculation() {
        // 10 grams at 6,000/g = 60,000 metal value
        // 10% making charge = 6,000
        // Subtotal = 66,000
        // GST 3% = 1,980
        // Total = 67,980
        val breakdown = PriceBreakdown.calculate(
            weight = 10.0,
            ratePerGram = 6000.0,
            chargeType = MakingChargeType.PERCENTAGE,
            chargeValue = 10.0
        )

        assertEquals(BigDecimal("60000.00"), breakdown.metalValue)
        assertEquals(BigDecimal("6000.00"), breakdown.makingCharge)
        assertEquals(BigDecimal("66000.00"), breakdown.subtotal)
        assertEquals(BigDecimal("1980.00"), breakdown.gstAmount)
        assertEquals(BigDecimal("67980.00"), breakdown.totalEstimatedPrice)
    }

    @Test
    fun testSilverPriceBreakdownPerGram() {
        // 100 grams of silver at 90/g = 9,000
        // Making charge at 20/g = 2,000
        // Subtotal = 11,000
        // GST 3% = 330
        // Total = 11,330
        val breakdown = PriceBreakdown.calculate(
            weight = 100.0,
            ratePerGram = 90.0,
            chargeType = MakingChargeType.PER_GRAM,
            chargeValue = 20.0
        )

        assertEquals(BigDecimal("9000.00"), breakdown.metalValue)
        assertEquals(BigDecimal("2000.00"), breakdown.makingCharge)
        assertEquals(BigDecimal("11000.00"), breakdown.subtotal)
        assertEquals(BigDecimal("330.00"), breakdown.gstAmount)
        assertEquals(BigDecimal("11330.00"), breakdown.totalEstimatedPrice)
    }

    @Test
    fun testFixedMakingChargeCalculation() {
        // 5 grams at 7,000/g = 35,000
        // Flat 500 making charge = 500
        // Subtotal = 35,500
        // GST 3% = 1,065
        // Total = 36,565
        val breakdown = PriceBreakdown.calculate(
            weight = 5.0,
            ratePerGram = 7000.0,
            chargeType = MakingChargeType.FIXED_AMOUNT,
            chargeValue = 500.0
        )

        assertEquals(BigDecimal("35000.00"), breakdown.metalValue)
        assertEquals(BigDecimal("500.00"), breakdown.makingCharge)
        assertEquals(BigDecimal("35500.00"), breakdown.subtotal)
        assertEquals(BigDecimal("1065.00"), breakdown.gstAmount)
        assertEquals(BigDecimal("36565.00"), breakdown.totalEstimatedPrice)
    }
}
