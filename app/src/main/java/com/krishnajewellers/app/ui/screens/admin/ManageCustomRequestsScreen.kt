package com.krishnajewellers.app.ui.screens.admin

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishnajewellers.app.data.model.CustomDesignRequest
import com.krishnajewellers.app.data.model.MetalRates
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
import com.krishnajewellers.app.ui.theme.StatusDeclined
import com.krishnajewellers.app.ui.theme.StatusPending
import com.krishnajewellers.app.ui.theme.StatusQuoted
import com.krishnajewellers.app.viewmodel.AdminViewModel

@Composable
fun ManageCustomRequestsScreen(
    viewModel: AdminViewModel,
    onBackClick: () -> Unit
) {
    val requests by viewModel.customRequests.collectAsState()
    var selectedRequestForQuote by remember { mutableStateOf<CustomDesignRequest?>(null) }

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
                    text = "CUSTOM GOLD REQUESTS",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    letterSpacing = 1.sp,
                    color = DarkText
                )
                Text(
                    text = "${requests.size} Requests Received",
                    fontSize = 11.sp,
                    color = DeepGold
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(requests, key = { it.id }) { req ->
                AdminRequestCard(
                    request = req,
                    onQuoteClick = { selectedRequestForQuote = req }
                )
            }
        }
    }

    selectedRequestForQuote?.let { req ->
        QuoteDialog(
            request = req,
            onDismiss = { selectedRequestForQuote = null },
            onSubmitQuote = { status, price, note ->
                viewModel.quoteCustomRequest(req.id, status, price, note)
                selectedRequestForQuote = null
            }
        )
    }
}

@Composable
private fun AdminRequestCard(
    request: CustomDesignRequest,
    onQuoteClick: () -> Unit
) {
    val statusColor = when (request.status) {
        "Quoted" -> StatusQuoted
        "Accepted" -> StatusAccepted
        "Declined" -> StatusDeclined
        else -> StatusPending
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderGold, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${request.id} • ${request.customerName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = DarkText
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(text = request.status.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = statusColor)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = request.title, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkText)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = request.description, fontSize = 12.sp, color = MutedText, lineHeight = 16.sp)

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Weight: ≈ ${request.preferredWeight}g", fontSize = 11.5.sp, color = DarkText)
                Text(text = "Budget: " + MetalRates.formatInr(request.budget), fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = DeepGold)
            }

            if (request.quotedPrice != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Quoted: " + MetalRates.formatInr(request.quotedPrice) + (request.quoteNote?.let { " ($it)" } ?: ""),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = StatusQuoted
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DeepGold)
                    .clickable { onQuoteClick() }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (request.status == "Quoted") "Update Quote / Status" else "Create Quote for Customer",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun QuoteDialog(
    request: CustomDesignRequest,
    onDismiss: () -> Unit,
    onSubmitQuote: (status: String, price: Double, note: String) -> Unit
) {
    var priceStr by remember { mutableStateOf((request.quotedPrice ?: request.budget).toInt().toString()) }
    var note by remember { mutableStateOf(request.quoteNote ?: "Crafted in 22K hallmarked gold within 12 working days.") }
    var selectedStatus by remember { mutableStateOf("Quoted") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Quote for ${request.title}",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = DarkText
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = priceStr,
                    onValueChange = { priceStr = it },
                    label = { Text("Quoted Price (₹ INR)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Quote Note / Crafting Time") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Status:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DarkText)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Quoted", "Accepted", "Declined").forEach { st ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selectedStatus == st) DeepGold else SecondaryBg)
                                .clickable { selectedStatus = st }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = st,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedStatus == st) Color.White else DarkText
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val p = priceStr.toDoubleOrNull() ?: request.budget
                    onSubmitQuote(selectedStatus, p, note)
                }
            ) {
                Text("Submit Quote", fontWeight = FontWeight.Bold, color = DeepGold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MutedText)
            }
        }
    )
}
