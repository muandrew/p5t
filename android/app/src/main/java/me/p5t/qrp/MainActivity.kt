package me.p5t.qrp

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.p5t.qrp.theme.QRPasteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        setContent {
            QRPasteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(clipboard)

                }
            }
        }
    }
}

@Composable
fun App(clipboardManager: ClipboardManager) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QRGenerator()
        HorizontalDivider(
            thickness = 2.dp,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 4.dp,
            ),
        )
        QRScanPage(clipboardManager)
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    QRPasteTheme { }
}