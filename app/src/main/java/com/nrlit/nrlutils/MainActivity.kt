package com.nrlit.nrlutils

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nrlit.nrlutils.ui.theme.NRLUtilsTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.activity.compose.rememberLauncherForActivityResult

data class MenuItem(val name: String, val icon: ImageVector, val activityClass: Class<*>)

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NRLUtilsTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text("NRL Utils", fontWeight = FontWeight.Bold) })
                    },
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    MenuGrid(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MenuGrid(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val permissions = if (Build.VERSION.SDK_INT >= 33) {
        arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_SMS,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.RECORD_AUDIO
        )
    } else {
        arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_SMS,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
        )
    }
    val permissionsGranted = remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        permissionsGranted.value = results.values.all { it }
    }
    LaunchedEffect(Unit) {
        launcher.launch(permissions)
    }
    if (permissionsGranted.value) {
        val menuItems = listOf(
            MenuItem("Contacts", Icons.Filled.Person, ContactsActivity::class.java),
            MenuItem("Call Logs", Icons.Filled.Call, CallLogsActivity::class.java),
            MenuItem("Messages", Icons.AutoMirrored.Filled.Message, MessagesActivity::class.java),
            MenuItem("Camera", Icons.Filled.Camera, CameraActivity::class.java),
            MenuItem("Photos", Icons.Filled.Photo, PhotosActivity::class.java),
            MenuItem("Video Recorder", Icons.Filled.Videocam, VideoRecorderActivity::class.java),
            MenuItem("Videos", Icons.Filled.VideoLibrary, VideosActivity::class.java),
            MenuItem("Audio Recorder", Icons.Filled.Mic, AudioRecorderActivity::class.java),
            MenuItem("Audios", Icons.Filled.AudioFile, AudiosActivity::class.java),
            MenuItem("Screen Recorder", Icons.AutoMirrored.Filled.ScreenShare, ScreenRecorderActivity::class.java),
            MenuItem("Screen Records", Icons.Filled.VideoFile, ScreenRecordsActivity::class.java),
            MenuItem("Files", Icons.Filled.Folder, FilesActivity::class.java),
            MenuItem("Screenshots", Icons.Filled.Screenshot, ScreenshotsActivity::class.java),
            MenuItem("Maps", Icons.Filled.Map, MapsActivity::class.java),
            MenuItem("Settings", Icons.Filled.Settings, SettingsActivity::class.java),
            MenuItem("About", Icons.Filled.Info, AboutActivity::class.java),
            MenuItem("Help", Icons.AutoMirrored.Filled.Help, HelpActivity::class.java),
            MenuItem("Privacy Policy", Icons.Filled.PrivacyTip, PrivacyPolicyActivity::class.java)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(menuItems) { item ->
                MenuCard(item) {
                    val intent = Intent(context, item.activityClass)
                    context.startActivity(intent)
                }
            }
        }
    } else {
        PermissionRequestScreen(
            modifier = modifier,
            onRequestPermissions = { launcher.launch(permissions) }
        )
    }
}

@Composable
fun MenuCard(item: MenuItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClick),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.name,
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun PermissionRequestScreen(modifier: Modifier = Modifier, onRequestPermissions: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Permissions Required",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "This app requires all permissions to function properly. Please grant all permissions to continue.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
            textAlign = TextAlign.Center
        )
        Button(onClick = onRequestPermissions) {
            Text("Grant Permissions")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuGridPreview() {
    NRLUtilsTheme {
        MenuGrid()
    }
}