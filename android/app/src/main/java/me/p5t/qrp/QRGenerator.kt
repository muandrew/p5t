package me.p5t.qrp

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import me.p5t.qrp.theme.QRPasteTheme
import java.net.URL
import java.net.URLEncoder

@Composable
fun QRGenerator() {
    var qr by remember {
        mutableStateOf("")
    }
    var disablePrefix by remember {
        mutableStateOf(false)
    }
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val data = if (disablePrefix) {
                qr
            } else {
                Uri.Builder()
                    .scheme("https")
                    .authority("p5t.me")
                    .appendQueryParameter("q", qr)
                    .build()
                    .toString()
            }
            val sizeModifier = Modifier
                .width(256.dp)
                .height(256.dp)
            Image(
                painter = rememberQrCodePainter(data),
                modifier = sizeModifier,
                contentDescription = "qrcode",
            )
            TextField(value = qr, onValueChange = { qr = it })
            Text(text = data)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = disablePrefix, onCheckedChange = { disablePrefix = it })
                Text(text = "disable p5t prefix")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QRGeneratorPreview() {
    QRPasteTheme {
        QRGenerator()
    }
}