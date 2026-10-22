package com.krishnajewellers.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krishnajewellers.app.ui.components.KJBrandHeader
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

@Composable
fun LoginScreen(
    onLoginSuccess: (name: String, email: String, phone: String) -> Unit,
    onContinueAsGuest: () -> Unit,
    onNavigateToStaffPortal: () -> Unit = {}
) {
    var email by remember { mutableStateOf("kunal.mehta@example.com") }
    var password by remember { mutableStateOf("password123") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isCreateAccountMode by remember { mutableStateOf(false) }
    var fullName by remember { mutableStateOf("Kunal Mehta") }
    var phone by remember { mutableStateOf("+91 98251 98765") }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBg)
            .statusBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            KJBrandHeader(logoSize = 70.dp)

            Spacer(modifier = Modifier.height(28.dp))

            // Demo Mode Notice Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SecondaryBg)
                    .border(1.dp, BorderGold, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = DeepGold,
                        modifier = Modifier.padding(end = 8.dp).height(18.dp)
                    )
                    Text(
                        text = "Demo Authentication: Local pre-filled sample account. No online login required.",
                        fontSize = 11.5.sp,
                        color = DarkText,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if (isCreateAccountMode) "Create Demo Account" else "Customer Login",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isCreateAccountMode) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepGold,
                        unfocusedBorderColor = BorderGold,
                        focusedLabelColor = DeepGold,
                        unfocusedContainerColor = CardBg,
                        focusedContainerColor = CardBg
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepGold,
                        unfocusedBorderColor = BorderGold,
                        focusedLabelColor = DeepGold,
                        unfocusedContainerColor = CardBg,
                        focusedContainerColor = CardBg
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                leadingIcon = {
                    Icon(Icons.Outlined.Email, contentDescription = null, tint = DeepGold)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeepGold,
                    unfocusedBorderColor = BorderGold,
                    focusedLabelColor = DeepGold,
                    unfocusedContainerColor = CardBg,
                    focusedContainerColor = CardBg
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(Icons.Outlined.Lock, contentDescription = null, tint = DeepGold)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                            contentDescription = null,
                            tint = MutedText
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeepGold,
                    unfocusedBorderColor = BorderGold,
                    focusedLabelColor = DeepGold,
                    unfocusedContainerColor = CardBg,
                    focusedContainerColor = CardBg
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            PremiumGoldButton(
                text = if (isCreateAccountMode) "Register Demo Account" else "Login to Showroom",
                onClick = {
                    onLoginSuccess(
                        fullName.ifBlank { "Kunal Mehta" },
                        email.ifBlank { "kunal.mehta@example.com" },
                        phone.ifBlank { "+91 98251 98765" }
                    )
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(
                onClick = { isCreateAccountMode = !isCreateAccountMode }
            ) {
                Text(
                    text = if (isCreateAccountMode) "Already have an account? Login" else "New to Krishna Jewellers? Create Account",
                    fontSize = 12.5.sp,
                    color = DeepGold,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = BorderGold)
                Text(
                    text = "  OR  ",
                    fontSize = 11.sp,
                    color = MutedText,
                    fontWeight = FontWeight.SemiBold
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = BorderGold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Continue as Guest Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.2.dp, BorderGold, RoundedCornerShape(14.dp))
                    .background(CardBg)
                    .clickable { onContinueAsGuest() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Continue as Guest Explorer",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = DarkText
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Showroom Staff Portal Link
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(SecondaryBg)
                    .border(0.8.dp, BorderGold, RoundedCornerShape(10.dp))
                    .clickable { onNavigateToStaffPortal() }
                    .padding(horizontal = 16.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = null,
                    tint = DeepGold,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Showroom Staff & Admin Access",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = DeepGold
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
