package com.krishnajewellers.app.ui.screens.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishnajewellers.app.data.model.JewelleryCategories
import com.krishnajewellers.app.data.model.MakingChargeType
import com.krishnajewellers.app.data.model.MetalRates
import com.krishnajewellers.app.data.model.MetalType
import com.krishnajewellers.app.data.model.Product
import com.krishnajewellers.app.ui.components.CategoryFilterChip
import com.krishnajewellers.app.ui.components.PremiumGoldButton
import com.krishnajewellers.app.ui.theme.BorderGold
import com.krishnajewellers.app.ui.theme.CardBg
import com.krishnajewellers.app.ui.theme.DarkText
import com.krishnajewellers.app.ui.theme.DeepGold
import com.krishnajewellers.app.ui.theme.GoldAccent
import com.krishnajewellers.app.ui.theme.MutedText
import com.krishnajewellers.app.ui.theme.PrimaryBg
import com.krishnajewellers.app.ui.theme.PremiumGold
import com.krishnajewellers.app.ui.theme.SecondaryBg
import com.krishnajewellers.app.utils.DrawableUtils
import com.krishnajewellers.app.viewmodel.AdminViewModel

@Composable
fun ManageProductsScreen(
    viewModel: AdminViewModel,
    onBackClick: () -> Unit
) {
    val products by viewModel.products.collectAsState()
    val rates by viewModel.rates.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBg)
            .statusBarsPadding()
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(CardBg)
                        .border(1.dp, BorderGold, CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DarkText)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "MANAGE PRODUCTS",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        letterSpacing = 1.sp,
                        color = DarkText
                    )
                    Text(
                        text = "${products.size} Products in Catalogue",
                        fontSize = 11.sp,
                        color = DeepGold
                    )
                }
            }

            // Add Product Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(DeepGold)
                    .clickable { showAddDialog = true }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Item", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(products, key = { it.id }) { product ->
                AdminProductItemCard(
                    product = product,
                    rates = rates,
                    onToggleVisibility = { viewModel.toggleProductVisibility(product.id, !product.isVisible) },
                    onDelete = { viewModel.deleteProduct(product.id) }
                )
            }
        }
    }

    if (showAddDialog) {
        AddProductDialog(
            onDismiss = { showAddDialog = false },
            onAddProduct = { name, category, metal, purity, desc, minW, midW, maxW, chargeType, chargeVal, img ->
                viewModel.addProduct(
                    name = name,
                    category = category,
                    metalType = metal,
                    purity = purity,
                    description = desc,
                    minWeight = minW,
                    midWeight = midW,
                    maxWeight = maxW,
                    chargeType = chargeType,
                    chargeValue = chargeVal,
                    imageRes = img
                )
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun AdminProductItemCard(
    product: Product,
    rates: MetalRates,
    onToggleVisibility: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val imageResId = DrawableUtils.getDrawableResId(context, product.imageResName)
    val price = product.calculatePrice(rates).totalEstimatedPrice

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderGold, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (product.isVisible) CardBg else Color(0xFFF3ECE0)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SecondaryBg)
                    .padding(4.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DarkText,
                    maxLines = 1
                )
                Text(
                    text = "${product.category} • ${product.purity} • ${product.midWeight}g",
                    fontSize = 11.sp,
                    color = MutedText
                )
                Text(
                    text = "Est. " + MetalRates.formatInrDecimal(price),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp,
                    color = DeepGold
                )
            }

            // Visibility Toggle
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (product.isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = "Toggle Visibility",
                    tint = if (product.isVisible) DeepGold else MutedText
                )
            }

            // Delete button (for demo items)
            if (product.isDemoCreated) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFC41E3A)
                    )
                }
            }
        }
    }
}

@Composable
private fun AddProductDialog(
    onDismiss: () -> Unit,
    onAddProduct: (
        name: String,
        category: String,
        metal: MetalType,
        purity: String,
        desc: String,
        minW: Double,
        midW: Double,
        maxW: Double,
        chargeType: MakingChargeType,
        chargeVal: Double,
        imageRes: String
    ) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedMetal by remember { mutableStateOf(MetalType.GOLD) }
    var selectedCategory by remember { mutableStateOf("Necklaces") }
    var purity by remember { mutableStateOf("22K (916 Hallmarked)") }
    var description by remember { mutableStateOf("") }
    var midWeightStr by remember { mutableStateOf("18.5") }
    var makingChargeStr by remember { mutableStateOf("12.0") }
    var selectedImage by remember { mutableStateOf("jewel_gold_bridal_necklace") }

    val categories = if (selectedMetal == MetalType.GOLD) {
        JewelleryCategories.goldCategories.filter { it != "All Gold" }
    } else {
        JewelleryCategories.silverCategories.filter { it != "All Silver" }
    }

    val sampleImages = if (selectedMetal == MetalType.GOLD) {
        listOf("jewel_gold_bridal_necklace", "jewel_gold_choker", "jewel_gold_jhumka_earrings", "jewel_gold_ring_ladies", "jewel_gold_rope_chain", "jewel_gold_filigree_bangles")
    } else {
        listOf("jewel_silver_payal", "jewel_silver_rope_chain", "jewel_silver_kada", "jewel_silver_bracelet", "jewel_silver_coin")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Jewellery to Catalogue",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = DarkText
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                // Metal Switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SecondaryBg)
                        .padding(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedMetal == MetalType.GOLD) DeepGold else Color.Transparent)
                            .clickable {
                                selectedMetal = MetalType.GOLD
                                selectedCategory = "Necklaces"
                                purity = "22K (916 Hallmarked)"
                                selectedImage = "jewel_gold_bridal_necklace"
                            }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("GOLD", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (selectedMetal == MetalType.GOLD) Color.White else DarkText)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedMetal == MetalType.SILVER) Color(0xFF5D646B) else Color.Transparent)
                            .clickable {
                                selectedMetal = MetalType.SILVER
                                selectedCategory = "Payal"
                                purity = "92.5 Sterling Silver"
                                selectedImage = "jewel_silver_payal"
                            }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("SILVER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (selectedMetal == MetalType.SILVER) Color.White else DarkText)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Product Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Category Chips
                Text("Category:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DarkText)
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        CategoryFilterChip(
                            title = cat,
                            isSelected = cat == selectedCategory,
                            onClick = { selectedCategory = cat }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = midWeightStr,
                        onValueChange = { midWeightStr = it },
                        label = { Text("Weight (g)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = makingChargeStr,
                        onValueChange = { makingChargeStr = it },
                        label = { Text("Making (%)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Brief Description") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        val midW = midWeightStr.toDoubleOrNull() ?: 18.0
                        val charge = makingChargeStr.toDoubleOrNull() ?: 12.0
                        onAddProduct(
                            name,
                            selectedCategory,
                            selectedMetal,
                            purity,
                            description.ifBlank { "Authentic hallmarked handcrafted jewellery from Krishna Jewellers." },
                            midW * 0.8,
                            midW,
                            midW * 1.25,
                            MakingChargeType.PERCENTAGE,
                            charge,
                            selectedImage
                        )
                    }
                }
            ) {
                Text("Add Product", fontWeight = FontWeight.Bold, color = DeepGold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MutedText)
            }
        }
    )
}
