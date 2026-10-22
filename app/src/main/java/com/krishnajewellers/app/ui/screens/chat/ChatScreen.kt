package com.krishnajewellers.app.ui.screens.chat

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishnajewellers.app.data.model.EnquiryMessage
import com.krishnajewellers.app.data.model.MetalRates
import com.krishnajewellers.app.ui.components.KJLogoBadge
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
import com.krishnajewellers.app.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(
    conversationId: String,
    viewModel: ChatViewModel,
    onBackClick: () -> Unit
) {
    LaunchedEffect(conversationId) {
        viewModel.selectConversation(conversationId)
    }

    val conversation by viewModel.activeConversation.collectAsState()
    val messages by viewModel.activeMessages.collectAsState()
    val inputMessage by viewModel.messageInput.collectAsState()
    val listState = rememberLazyListState()
    val context = LocalContext.current

    var sendAsOwnerMode by remember { mutableStateOf(false) }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val quickReplies = listOf(
        "Is this in stock at Godadara Road?",
        "Can I customize the weight to 30g?",
        "What are the today's making charges?",
        "Can I book a viewing appointment?"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBg)
            .statusBarsPadding()
            .imePadding()
    ) {
        // Chat Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBg)
                .border(width = 0.8.dp, color = BorderGold.copy(alpha = 0.6f))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DarkText)
            }

            Spacer(modifier = Modifier.width(6.dp))

            KJLogoBadge(size = 38.dp)

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Krishna Jewellers Showroom",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.5.sp,
                    color = DarkText
                )
                Text(
                    text = "Surat • Master Karigar Consultation",
                    fontSize = 10.5.sp,
                    color = PremiumGold
                )
            }

            // Demo sender toggle switch
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (sendAsOwnerMode) DeepGold else SecondaryBg)
                    .border(1.dp, BorderGold, RoundedCornerShape(8.dp))
                    .clickable { sendAsOwnerMode = !sendAsOwnerMode }
                    .padding(horizontal = 8.dp, vertical = 5.dp)
            ) {
                Text(
                    text = if (sendAsOwnerMode) "Shop Mode" else "Customer",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (sendAsOwnerMode) Color.White else DeepGold
                )
            }
        }

        // Attached Product Banner if available
        conversation?.let { conv ->
            if (conv.productName != null && conv.productImage != null) {
                val imgResId = DrawableUtils.getDrawableResId(context, conv.productImage)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SecondaryBg)
                        .border(width = 0.8.dp, color = BorderGold.copy(alpha = 0.5f))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = imgResId),
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CardBg)
                                .padding(4.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Enquiring about: " + conv.productName,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.5.sp,
                                color = DarkText
                            )
                            Text(
                                text = "Tap messages to send showroom inquiries",
                                fontSize = 10.5.sp,
                                color = MutedText
                            )
                        }
                    }
                }
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                MessageBubble(message = msg)
            }
        }

        // Quick Reply Suggestions
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryBg)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickReplies) { reply ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardBg)
                        .border(1.dp, BorderGold, RoundedCornerShape(12.dp))
                        .clickable { viewModel.sendQuickReply(reply, asOwner = sendAsOwnerMode) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = reply,
                        fontSize = 11.sp,
                        color = DarkText,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Message Input Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBg)
                .border(width = 0.8.dp, color = BorderGold.copy(alpha = 0.6f))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputMessage,
                onValueChange = { viewModel.onMessageInputChanged(it) },
                placeholder = {
                    Text(
                        if (sendAsOwnerMode) "Reply as Krishna Jewellers..." else "Type enquiry message...",
                        fontSize = 13.sp
                    )
                },
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeepGold,
                    unfocusedBorderColor = BorderGold,
                    focusedContainerColor = SecondaryBg,
                    unfocusedContainerColor = SecondaryBg
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(DeepGold)
                    .clickable { viewModel.sendMessage(asOwner = sendAsOwnerMode) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun MessageBubble(message: EnquiryMessage) {
    val isCustomer = message.sender == "customer"
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val timeStr = timeFormat.format(Date(message.timestamp))
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isCustomer) Arrangement.End else Arrangement.Start
    ) {
        Column(
            horizontalAlignment = if (isCustomer) Alignment.End else Alignment.Start,
            modifier = Modifier.fillMaxWidth(0.82f)
        ) {
            // Sender tag
            Text(
                text = if (isCustomer) "You (Customer)" else "Krishna Jewellers",
                fontSize = 9.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isCustomer) DeepGold else PremiumGold,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )

            // Bubble Card
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isCustomer) 16.dp else 2.dp,
                            bottomEnd = if (isCustomer) 2.dp else 16.dp
                        )
                    )
                    .background(if (isCustomer) DeepGold else CardBg)
                    .border(
                        width = 1.dp,
                        color = if (isCustomer) DeepGold else BorderGold,
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isCustomer) 16.dp else 2.dp,
                            bottomEnd = if (isCustomer) 2.dp else 16.dp
                        )
                    )
                    .padding(12.dp)
            ) {
                Column {
                    // If message contains attached product card
                    if (message.attachedProductName != null) {
                        val imgId = message.attachedProductImage?.let {
                            DrawableUtils.getDrawableResId(context, it)
                        } ?: com.krishnajewellers.app.R.drawable.ic_kj_logo

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isCustomer) Color(0xFF6B5120) else SecondaryBg)
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = imgId),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(CardBg)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = message.attachedProductName,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCustomer) Color.White else DarkText
                                )
                                message.attachedProductPrice?.let { p ->
                                    Text(
                                        text = "Est. " + MetalRates.formatInr(p),
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isCustomer) GoldAccent else DeepGold
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Text(
                        text = message.message,
                        fontSize = 13.5.sp,
                        color = if (isCustomer) Color.White else DarkText,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = timeStr,
                        fontSize = 9.sp,
                        color = if (isCustomer) GoldAccent.copy(alpha = 0.8f) else MutedText,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}
