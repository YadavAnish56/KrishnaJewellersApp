package com.krishnajewellers.app.ui.screens.custom

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.krishnajewellers.app.data.model.CustomDesignRequest
import com.krishnajewellers.app.data.model.MetalRates
import com.krishnajewellers.app.ui.components.CategoryFilterChip
import com.krishnajewellers.app.ui.components.PremiumGoldButton
import com.krishnajewellers.app.ui.theme.BorderGold
import com.krishnajewellers.app.ui.theme.CardBg
import com.krishnajewellers.app.ui.theme.DarkText
import com.krishnajewellers.app.ui.theme.DeepGold
import com.krishnajewellers.app.ui.theme.GoldAccent
import com.krishnajewellers.app.ui.theme.GoldGradient
import com.krishnajewellers.app.ui.theme.MutedText
import com.krishnajewellers.app.ui.theme.PrimaryBg
import com.krishnajewellers.app.ui.theme.PremiumGold
import com.krishnajewellers.app.ui.theme.SecondaryBg
import com.krishnajewellers.app.ui.theme.StatusAccepted
import com.krishnajewellers.app.ui.theme.StatusDeclined
import com.krishnajewellers.app.ui.theme.StatusPending
import com.krishnajewellers.app.ui.theme.StatusQuoted
import com.krishnajewellers.app.viewmodel.CustomRequestViewModel

@Composable
fun CustomRequestScreen(
    viewModel: CustomRequestViewModel,
    userName: String = "Kunal Mehta",
    userPhone: String = "+91 98251 98765",
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Submit Request, 1: My Requests

    val title by viewModel.title.collectAsState()
    val description by viewModel.description.collectAsState()
    val preferredWeight by viewModel.preferredWeight.collectAsState()
    val budget by viewModel.budget.collectAsState()
    val selectedOccasion by viewModel.selectedOccasion.collectAsState()
    val attachments by viewModel.attachments.collectAsState()
    val submissionSuccess by viewModel.submissionSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val myRequests by viewModel.customRequests.collectAsState()

    // Android Photo Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.addAttachmentUri(it.toString()) }
    }

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
                .padding(horizontal = 16.dp, vertical = 8.dp),
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
                    text = "CUSTOM GOLD DESIGN",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    letterSpacing = 1.sp,
                    color = DarkText
                )
                Text(
                    text = "Bespoke 22K Hallmarked Gold Artistry",
                    fontSize = 11.sp,
                    color = PremiumGold
                )
            }
        }

        // Subtabs: "New Request" vs "My Submissions"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SecondaryBg)
                .border(1.dp, BorderGold, RoundedCornerShape(12.dp))
                .padding(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(9.dp))
                    .background(if (selectedTab == 0) DeepGold else Color.Transparent)
                    .clickable { selectedTab = 0 }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Request Custom Gold",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = if (selectedTab == 0) Color.White else DarkText
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(9.dp))
                    .background(if (selectedTab == 1) DeepGold else Color.Transparent)
                    .clickable { selectedTab = 1 }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "My Requests (${myRequests.size})",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = if (selectedTab == 1) Color.White else DarkText
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (selectedTab == 0) {
            // New Request Form
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 40.dp)
            ) {
                item {
                    // Gold-Only Notice Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(GoldAccent.copy(alpha = 0.6f))
                            .border(1.dp, BorderGold, RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = null,
                                tint = DeepGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Bespoke crafting is available exclusively for 22K and 18K Gold designs.",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = DarkText
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (submissionSuccess) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, StatusAccepted, RoundedCornerShape(18.dp)),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = CardBg)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = StatusAccepted,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Custom Request Submitted!",
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = DarkText
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Our master karigars in Surat are reviewing your reference images and specifications. A formal quote will appear under 'My Requests'.",
                                    fontSize = 12.5.sp,
                                    color = MutedText,
                                    lineHeight = 18.sp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                PremiumGoldButton(
                                    text = "View My Requests",
                                    onClick = {
                                        viewModel.resetSubmissionStatus()
                                        selectedTab = 1
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                } else {
                    item {
                        // Title
                        OutlinedTextField(
                            value = title,
                            onValueChange = { viewModel.onTitleChanged(it) },
                            label = { Text("Design Title (e.g. Royal Peacock Kada)") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DeepGold,
                                unfocusedBorderColor = BorderGold,
                                focusedContainerColor = CardBg,
                                unfocusedContainerColor = CardBg
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Occasion Selector Chips
                        Text(
                            text = "Occasion / Purpose",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = DarkText
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(viewModel.occasions) { occ ->
                                CategoryFilterChip(
                                    title = occ,
                                    isSelected = occ == selectedOccasion,
                                    onClick = { viewModel.onOccasionSelected(occ) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Preferred Weight & Approximate Budget Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = preferredWeight,
                                onValueChange = { viewModel.onWeightChanged(it) },
                                label = { Text("Pref. Weight (grams)") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = DeepGold,
                                    unfocusedBorderColor = BorderGold,
                                    focusedContainerColor = CardBg,
                                    unfocusedContainerColor = CardBg
                                ),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp)
                            )

                            OutlinedTextField(
                                value = budget,
                                onValueChange = { viewModel.onBudgetChanged(it) },
                                label = { Text("Budget (₹ INR)") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = DeepGold,
                                    unfocusedBorderColor = BorderGold,
                                    focusedContainerColor = CardBg,
                                    unfocusedContainerColor = CardBg
                                ),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Detailed Description
                        OutlinedTextField(
                            value = description,
                            onValueChange = { viewModel.onDescChanged(it) },
                            label = { Text("Describe Your Vision & Details") },
                            placeholder = { Text("Mention details like kundan finish, nakshi carving, ruby or emerald accents, wrist diameter, or heritage motifs...") },
                            minLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DeepGold,
                                unfocusedBorderColor = BorderGold,
                                focusedContainerColor = CardBg,
                                unfocusedContainerColor = CardBg
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Photo Picker Section (up to 5 images)
                        Text(
                            text = "Reference Photos (Up to 5 photos)",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = DarkText
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Upload Button
                            if (attachments.size < 5) {
                                Box(
                                    modifier = Modifier
                                        .size(76.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(SecondaryBg)
                                        .border(1.dp, BorderGold, RoundedCornerShape(14.dp))
                                        .clickable {
                                            photoPickerLauncher.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Default.AddPhotoAlternate,
                                            contentDescription = "Add Photo",
                                            tint = DeepGold,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Text(
                                            text = "Add Photo",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = DarkText
                                        )
                                    }
                                }
                            }

                            // Attached Images Preview
                            attachments.forEachIndexed { index, uriStr ->
                                Box(
                                    modifier = Modifier
                                        .size(76.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(SecondaryBg)
                                        .border(1.dp, BorderGold, RoundedCornerShape(14.dp))
                                ) {
                                    AsyncImage(
                                        model = uriStr,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    // Remove Badge
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .background(Color.Black.copy(alpha = 0.6f))
                                            .clickable { viewModel.removeAttachment(index) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }

                        if (errorMessage != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = errorMessage!!,
                                color = Color.Red,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        PremiumGoldButton(
                            text = "Submit Design for Quotation",
                            onClick = { viewModel.submitRequest(userName, userPhone) }
                        )
                    }
                }
            }
        } else {
            // My Requests List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (myRequests.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "You haven't submitted any custom design requests yet.",
                                fontSize = 13.sp,
                                color = MutedText
                            )
                        }
                    }
                } else {
                    items(myRequests, key = { it.id }) { req ->
                        CustomRequestCard(request = req)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomRequestCard(
    request: CustomDesignRequest,
    modifier: Modifier = Modifier
) {
    val statusColor = when (request.status) {
        "Quoted" -> StatusQuoted
        "Accepted" -> StatusAccepted
        "Declined" -> StatusDeclined
        else -> StatusPending
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BorderGold, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Row: ID & Status Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = request.id,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = PremiumGold,
                    letterSpacing = 1.sp
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = request.status.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = statusColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = request.title,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = request.description,
                fontFamily = FontFamily.SansSerif,
                fontSize = 12.5.sp,
                color = MutedText,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Occasion: ${request.occasion}",
                    fontSize = 11.5.sp,
                    color = DarkText,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Weight: ≈ ${request.preferredWeight}g",
                    fontSize = 11.5.sp,
                    color = DarkText,
                    fontWeight = FontWeight.Medium
                )
            }

            // Quoted Section
            if (request.status == "Quoted" && request.quotedPrice != null) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = SecondaryBg, thickness = 1.dp)
                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SecondaryBg)
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "STORE QUOTATION",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.sp,
                                color = DeepGold
                            )

                            Text(
                                text = MetalRates.formatInr(request.quotedPrice),
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = DeepGold
                            )
                        }

                        if (!request.quoteNote.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = request.quoteNote,
                                fontSize = 11.5.sp,
                                color = DarkText,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Submitted on ${request.date}",
                fontSize = 10.sp,
                color = MutedText
            )
        }
    }
}
