package com.nrlit.nrlutils

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nrlit.nrlutils.ui.theme.NRLUtilsTheme

class ScreenRecordsActivity : ComponentActivity() {
    private val selectedUris = mutableStateListOf<Uri>()

    private val pickMultipleScreenRecords = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        selectedUris.clear()
        selectedUris.addAll(uris)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NRLUtilsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScreenRecordsScreen(
                        modifier = Modifier.padding(innerPadding),
                        selectedUris = selectedUris,
                        onPickScreenRecords = {
                            pickMultipleScreenRecords.launch("video/*")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ScreenRecordsScreen(modifier: Modifier = Modifier, selectedUris: List<Uri>, onPickScreenRecords: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Screen Records", style = MaterialTheme.typography.headlineMedium)
        Button(onClick = onPickScreenRecords) {
            Text("Pick Screen Records")
        }
        Text(text = "Selected: ${selectedUris.size} screen records")
    }
}
