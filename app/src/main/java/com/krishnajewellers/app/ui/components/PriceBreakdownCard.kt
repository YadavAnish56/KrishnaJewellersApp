package com.krishnajewellers.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishnajewellers.app.data.model.MetalRates
import com.krishnajewellers.app.data.model.PriceBreakdown
import com.krishnajewellers.app.data.model.Product
import com.krishnajewellers.app.data.model.WeightOption
import com.krishnajewellers.app.ui.theme.BorderGold
import com.krishnajewellers.app.ui.theme.CardBg
import com.krishnajewellers.app.ui.theme.DarkText
import com.krishnajewellers.app.ui.theme.DeepGold
import com.krishnajewellers.app.ui.theme.GoldAccent
import com.krishnajewellers.app.ui.theme.MutedText
import com.krishnajewellers.app.ui.theme.PremiumGold
import com.krishnajewellers.app.ui.theme.SecondaryBg

@Composable
fun PriceBreakdownCard(
    product: Product,
    breakdown: PriceBreakdown,
    selectedOption: WeightOption,
    onWeightOptionSelected: (WeightOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BorderGold, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            // Card Title
            Text(
                text = "PRICE BREAKDOWN & ESTIMATE",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                letterSpacing = 1.sp,
                color = DeepGold
            )
            Text(
                text = "Based on today's live precious metal rates",
                fontSize = 11.5.sp,
                color = MutedText
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive Weight Selector: MIN | MID | MAX
            Text(
                text = "Select Jewellery Weight:",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WeightTab(
                    modifier = Modifier.weight(1f),
                    label = "MIN",
                    weightText = "${product.minWeight}g",
                    isSelected = selectedOption == WeightOption.MIN,
                    onClick = { onWeightOptionSelected(WeightOption.MIN) }
                )
                WeightTab(
                    modifier = Modifier.weight(1f),
                    label = "MID (Standard)",
                    weightText = "${product.midWeight}g",
                    isSelected = selectedOption == WeightOption.MID,
                    onClick = { onWeightOptionSelected(WeightOption.MID) }
                )
                WeightTab(
                    modifier = Modifier.weight(1f),
                    label = "MAX",
                    weightText = "${product.maxWeight}g",
                    isSelected = selectedOption == WeightOption.MAX,
                    onClick = { onWeightOptionSelected(WeightOption.MAX) }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = SecondaryBg, thickness = 1.dp)
            Spacer(modifier = Modifier.height(14.dp))

            // Calculation Breakdown Rows
            BreakdownRow(
                label = "Selected Weight",
                value = "${breakdown.weightGrams} grams"
            )
            BreakdownRow(
                label = "Metal Rate per gram",
                value = MetalRates.formatInrDecimal(breakdown.metalRatePerGram)
            )
            BreakdownRow(
                label = "Pure Metal Value",
                value = MetalRates.formatInrDecimal(breakdown.metalValue)
            )
            BreakdownRow(
                label = "Making Charges (${breakdown.makingChargeDescription})",
                value = "+ " + MetalRates.formatInrDecimal(breakdown.makingCharge)
            )
            BreakdownRow(
                label = "Subtotal",
                value = MetalRates.formatInrDecimal(breakdown.subtotal)
            )
            BreakdownRow(
                label = "GST (3%)",
                value = "+ " + MetalRates.formatInrDecimal(breakdown.gstAmount)
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = BorderGold, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            // Total Estimated Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Estimated Total Price",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = DarkText
                    )
                    Text(
                        text = "All inclusive of taxes",
                        fontSize = 11.sp,
                        color = MutedText
                    )
                }

                Text(
                    text = MetalRates.formatInrDecimal(breakdown.totalEstimatedPrice),
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = DeepGold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Disclaimer Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SecondaryBg)
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = DeepGold,
                        modifier = Modifier.padding(end = 8.dp).height(16.dp)
                    )
                    Text(
                        text = "Estimated price. Final price is confirmed at the store based on precise gold weighing.",
                        fontSize = 11.sp,
                        color = DarkText,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun WeightTab(
    modifier: Modifier = Modifier,
    label: String,
    weightText: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isSelected) DeepGold else SecondaryBg
    val textCol = if (isSelected) Color.White else DarkText
    val borderCol = if (isSelected) DeepGold else BorderGold

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(1.dp, borderCol, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) GoldAccent else MutedText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = weightText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = textCol
            )
        }
    }
}

@Composable
private fun BreakdownRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MutedText
        )
        Text(
            text = value,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Medium,
            color = DarkText
        )
    }
}
