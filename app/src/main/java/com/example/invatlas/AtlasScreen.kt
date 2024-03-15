package com.example.invatlas

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.invatlas.utils.checkForPermission
import com.example.invatlas.utils.getCurrentLocation
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun AtlasScreen() {
    val context = LocalContext.current

    var showPlantWindow by remember { mutableStateOf(false) }

    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { newImage ->
            bitmap = newImage
            // From here we can use the bitmap to analyze the picture

        }
    )

    val userPlants : List<UserPlant> = listOf(UserPlant(1, 1, 1, "", 46.5458, -72.7492))

    val hasLocationPermission = remember {
        mutableStateOf(
            checkForPermission(context)
        )
    }
    var showMap by remember { mutableStateOf(false) }

    val properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.HYBRID, isMyLocationEnabled = true))
    }

    val defaultLocation = LatLng(46.5458, -72.7492) // Shawinigan, QC
    var currentUserPosition by remember { mutableStateOf(defaultLocation) }

    getCurrentLocation(context) {
        currentUserPosition = it
        showMap = true
    }

    var selectedPlant: UserPlant = UserPlant(1, 1, 1, "", 0.0, 0.0)

    if (showMap) {
        val cameraPositionState = rememberCameraPositionState {
            position = if (hasLocationPermission.value) {
                CameraPosition.Builder().target(currentUserPosition).zoom(16f).build()
            } else {
                CameraPosition.Builder().target(defaultLocation).zoom(8f).build()
            }
        }
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp, top = 40.dp),
            properties = properties,
            cameraPositionState = cameraPositionState
        ) {
            userPlants.forEach { userPlant ->
                Marker(
                    state = MarkerState(position = defaultLocation),
                    title = "Shawi",
                    onClick = {
                        showPlantWindow = true
                        selectedPlant = userPlant
                        true},
                    // icon = bitmapDescriptorFromVector(context, R.drawable.baseline_grass_24) TODO: Grass icon
                )
            }
        }
        if (showPlantWindow) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                PlantWindow(selectedPlant, onClose = { showPlantWindow = false })
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    FloatingActionButton(
        onClick = { cameraLauncher.launch() },
        modifier = Modifier.padding(top = 50.dp, start = 10.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_photo_camera_24),
            contentDescription = "Floating action button."
        )
    }
}