package me.p5t.qrp

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transformLatest
import me.p5t.qrp.theme.QRPasteTheme

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun QRGenerator() {
    var qr by remember {
        mutableStateOf("")
    }
    val r = remember {
        snapshotFlow {
            qr
        }
            .transformLatest {
                emit(null)
                delay(500)
                emit(getQrCodeBitmap(it))
            }
            .flowOn(Dispatchers.Default)
            .conflate()
    }
    val bm by r.collectAsState(initial = null)
    val b = bm
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        val sizeModifier = Modifier
            .width(256.dp)
            .height(256.dp)
        if (b != null) {
            Image(
                modifier = sizeModifier,
                bitmap = b,
                contentDescription = "qrcode",
            )
        } else {
            CircularProgressIndicator(
                modifier = sizeModifier.padding(24.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
        TextField(value = qr, onValueChange = { qr = it })
    }
}

fun getQrCodeBitmap(data: String): ImageBitmap {
    val size = 512 //pixels
    val qrCodeContent = "https://p5t.me?q=${data}"
    val hints = hashMapOf<EncodeHintType, Int>().also {
        it[EncodeHintType.MARGIN] = 1
    } // Make the QR code buffer border narrower
    val bits = QRCodeWriter().encode(qrCodeContent, BarcodeFormat.QR_CODE, size, size, hints)
    return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
        for (x in 0 until size) {
            for (y in 0 until size) {
                it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
            }
        }
    }.asImageBitmap()
}

@Preview(showBackground = true)
@Composable
fun QRGeneratorPreview() {
    QRPasteTheme {
        QRGenerator()
    }
}