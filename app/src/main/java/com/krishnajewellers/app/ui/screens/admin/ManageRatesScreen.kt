package com.krishnajewellers.app.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.krishnajewellers.app.ui.theme.StatusAccepted
import com.krishnajewellers.app.viewmodel.AdminViewModel

@Composable
fun ManageRatesScreen(
    viewModel: AdminViewModel,
    onBackClick: () -> Unit
) {
    val gold24k by viewModel.gold24kInput.collectAsState()
    val gold22k by viewModel.gold22kInput.collectAsState()
    val gold18k by viewModel.gold18kInput.collectAsState()
    val silverKg by viewModel.silverKgInput.collectAsState()
    val feedback by viewModel.rateUpdateFeedback.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBg)
            .statusBarsPadding()
            .imePadding()
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                    text = "MANAGE METAL RATES",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    letterSpacing = 1.sp,
                    color = DarkText
                )
                Text(
                    text = "Live Shop Entered Rates • Surat Showroom",
                    fontSize = 11.sp,
                    color = DeepGold
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(SecondaryBg)
                        .border(1.dp, BorderGold, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = DeepGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Entering new metal rates will automatically update the Home screen rates card and all product price breakdown calculators in real-time.",
                            fontSize = 12.sp,
                            color = DarkText,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            if (feedback != null) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(StatusAccepted.copy(alpha = 0.15f))
                            .border(1.dp, StatusAccepted, RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusAccepted, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = feedback!!, fontSize = 12.sp, color = DarkText, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            // Editable Rates Form Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderGold, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBg)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Gold Rates (Price in ₹ INR per 10 grams)",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = DeepGold
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Gold 24K
                        OutlinedTextField(
                            value = gold24k,
                            onValueChange = {
                                viewModel.clearFeedback()
                                viewModel.onGold24kChanged(it)
                            },
                            label = { Text("Gold 24K (999 Purity) per 10g") },
                            leadingIcon = { Text("₹", fontWeight = FontWeight.Bold, color = DeepGold, modifier = Modifier.padding(start = 12.dp)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DeepGold,
                                unfocusedBorderColor = BorderGold,
                                focusedContainerColor = SecondaryBg,
                                unfocusedContainerColor = SecondaryBg
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Gold 22K
                        OutlinedTextField(
                            value = gold22k,
                            onValueChange = {
                                viewModel.clearFeedback()
                                viewModel.onGold22kChanged(it)
                            },
                            label = { Text("Gold 22K (916 Purity) per 10g") },
                            leadingIcon = { Text("₹", fontWeight = FontWeight.Bold, color = DeepGold, modifier = Modifier.padding(start = 12.dp)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DeepGold,
                                unfocusedBorderColor = BorderGold,
                                focusedContainerColor = SecondaryBg,
                                unfocusedContainerColor = SecondaryBg
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Gold 18K
                        OutlinedTextField(
                            value = gold18k,
                            onValueChange = {
                                viewModel.clearFeedback()
                                viewModel.onGold18kChanged(it)
                            },
                            label = { Text("Gold 18K (750 Purity) per 10g") },
                            leadingIcon = { Text("₹", fontWeight = FontWeight.Bold, color = DeepGold, modifier = Modifier.padding(start = 12.dp)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DeepGold,
                                unfocusedBorderColor = BorderGold,
                                focusedContainerColor = SecondaryBg,
                                unfocusedContainerColor = SecondaryBg
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Silver Rates (Price in ₹ INR per 1 Kilogram)",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = DeepGold
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Silver 999
                        OutlinedTextField(
                            value = silverKg,
                            onValueChange = {
                                viewModel.clearFeedback()
                                viewModel.onSilverKgChanged(it)
                            },
                            label = { Text("Silver 999 Purity per 1 kg") },
                            leadingIcon = { Text("₹", fontWeight = FontWeight.Bold, color = DeepGold, modifier = Modifier.padding(start = 12.dp)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DeepGold,
                                unfocusedBorderColor = BorderGold,
                                focusedContainerColor = SecondaryBg,
                                unfocusedContainerColor = SecondaryBg
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        PremiumGoldButton(
                            text = "Save & Publish Rates to App",
                            onClick = { viewModel.saveRates() }
                        )
                    }
                }
            }
        }
    }
}
