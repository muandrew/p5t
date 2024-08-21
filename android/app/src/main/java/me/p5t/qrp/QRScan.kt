package me.p5t.qrp

import android.content.ClipData
import android.content.ClipboardManager
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

@Composable
fun QRScanPage(clipboardManager: ClipboardManager) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var fullUrl by remember {
            mutableStateOf("")
        }
        var data by remember {
            mutableStateOf("")
        }
        Text(text = fullUrl)
        Text(text = data)
        val ctx = LocalContext.current
        Button(onClick = {
            val scanner = GmsBarcodeScanning.getClient(ctx)
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    val q = barcode.url?.url?.let {
                        val url = Uri.parse(it)
                        url.getQueryParameter("q") ?: "noq"
                    } ?: "noq"
                    // Task completed successfully
                    fullUrl = barcode.rawValue ?: "empty?"
                    data = q
                    clipboardManager.setPrimaryClip(
                        ClipData.newPlainText(
                            "cpy",
                            data
                        )
                    )
                }
        }) {
            Text(text = "Scan for data")
        }
    }
}