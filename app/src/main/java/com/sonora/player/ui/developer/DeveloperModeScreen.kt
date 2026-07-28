package com.sonora.player.ui.developer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sonora.player.billing.BillingManager

/**
 * Ushbu ekran oddiy foydalanuvchilardan yashirilgan bo'ladi.
 * U Sozlamalar (Settings) ekranida versiya raqamini 7 marta bosganda ochilishi kerak.
 */
@Composable
fun DeveloperModeScreen(
    navController: NavController,
    billingManager: BillingManager // Hilt orqali uzatiladi yoki ViewModel orqali olinadi
) {
    val isPremium by billingManager.isPremiumUser.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "🛠 Developer Mode",
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 28.sp),
            color = MaterialTheme.colorScheme.error, // Qizil rang (ogohlantirish uchun)
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // 1. Premium Status Override
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Billing Testing",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                ) {
                    Text(text = "Fake Premium Status (Pro yoqish)")
                    Switch(
                        checked = isPremium,
                        onCheckedChange = { billingManager.setFakePremiumForTesting(it) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Database & Cache Tools
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Database Tools",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { /* TODO: Room bazasidagi barcha qatorlarni o'chirish */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Clear Room Database (Hard Reset)", color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { /* TODO: Coil image keshlari va boshqalarni tozalash */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Clear Image Cache")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Performance & Logs
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Performance & Logs",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Current FPS: 60/120 (v-sync tied)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Audio Backend: Media3 ExoPlayer",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
