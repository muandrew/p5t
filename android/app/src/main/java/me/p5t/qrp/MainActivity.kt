package me.p5t.qrp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import me.p5t.qrp.theme.QRPasteTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
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
                    var textFieldText by remember {
                        mutableStateOf("")
                    }
                    var selectedText by remember {
                        mutableStateOf("")
                    }
                    var selectedq by remember {
                        mutableStateOf("")
                    }
                    Column {
                        Greeting("Android")
                        TextField(value = textFieldText, onValueChange = {
                            textFieldText = it
                        })
//                        Button(onClick = { selectedText = textFieldText }) {
//                            Text(text = "Change!")
//                        }
                        Text(text = selectedText)

                        Text(text = selectedq)
                        Button(onClick = {
//                            val options = GmsBarcodeScannerOptions.Builder()
//                                .setBarcodeFormats(
//                                    Barcode.FORMAT_QR_CODE,
//                                    Barcode.FORMAT_AZTEC
//                                )
//                                .build()
                            val scanner = GmsBarcodeScanning.getClient(this@MainActivity)
                            scanner.startScan()
                                .addOnSuccessListener { barcode ->
                                    val q = barcode.url?.url?.let {
                                        val url = Uri.parse(it)
                                        url.getQueryParameter("q")?: "noq"
                                    } ?: "noq"
                                    // Task completed successfully
                                    selectedText = barcode.rawValue ?: "empty?"
                                    selectedq = q
                                    clipboard.setPrimaryClip(ClipData.newPlainText("cpy", selectedq))
                                }
                                .addOnCanceledListener {
                                    // Task canceled
                                }
                                .addOnFailureListener { e ->
                                    // Task failed with an exception
                                }
                        }) {
                            Text(text = "barcode!")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QRPasteTheme {
        Greeting("Android")
    }
}