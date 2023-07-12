package io.mappy.mappysdkimplementationexample.kotlin.activities

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.bemappy.sdk.loadables.Map
import io.bemappy.sdk.loadables.Scene
import io.bemappy.sdk.models.BreadcrumbPayload
import io.bemappy.sdk.models.Venue
import io.bemappy.sdk.services.tracking.TrackingService
import io.bemappy.sdk.ui.compose.ComposableMapView.MapView
import io.bemappy.sdk.ui.compose.ComposableSceneView.SceneView
import io.bemappy.sdk.ui.compose.controllers.MapController
import io.bemappy.sdk.ui.compose.controllers.SceneController
import io.mappy.mappysdkimplementationexample.kotlin.views.ErrorView
import io.mappy.mappysdkimplementationexample.kotlin.views.LoadingView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@ExperimentalFoundationApi
class MapActivityKt : AppCompatActivity() {

    private lateinit var map: Map
    private lateinit var scene: Scene

    private val _mapState = MutableStateFlow<MapState>(MapState.Loading(0))
    private val mapState: StateFlow<MapState> = _mapState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val venue = intent.getParcelableExtra<Venue>("venue") ?: throw NullPointerException()

        loadMaps(this, venue)

        setContent {
            Surface(modifier = Modifier.background(Color.White)) {
                Body()
            }
        }
    }

    @Composable
    fun Body() {
        val is3d = remember { mutableStateOf(false) }

        val mapController = remember { MapController() }
        val sceneController = remember { SceneController() }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when (mapState.collectAsState().value) {
                is MapState.Loaded -> {
                    val mapModifier = if (is3d.value) Modifier.size(0.dp) else Modifier
                        .fillMaxSize()
                        .background(Color.White)

                    SceneView(
                        scene = scene,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        sceneController = sceneController,
                    )

                    MapView(
                        map = map,
                        modifier = mapModifier,
                        mapController = mapController,
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        IconButton(
                            onClick = { finish() },
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color.White, shape = CircleShape)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "")
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.End
                    ) {
                        Button(
                            onClick = { is3d.value = is3d.value.not() },
                            modifier = Modifier
                                .width(80.dp)
                        ) {
                            val text = if (is3d.value) "3D" else "2D"
                            Text(text = text, fontSize = 8.sp)
                        }
                        Spacer(modifier = Modifier.size(5.dp))
                        Button(
                            onClick = {
                                if (is3d.value) {
                                    sceneController.setInitialViewpoint()
                                } else {
                                    mapController.setInitialViewpoint()
                                }
                            },
                            modifier = Modifier
                                .width(80.dp)
                        ) {
                            Text(text = "Zoom", fontSize = 8.sp)
                        }
                        Spacer(modifier = Modifier.size(5.dp))
                        Button(
                            onClick = {
                                if (is3d.value) {
                                    sceneController.rotateToNorth()
                                } else {
                                    mapController.rotateToNorth()
                                }
                            },
                            modifier = Modifier
                                .width(80.dp)
                        ) {
                            Text(text = "Rotate", fontSize = 8.sp)
                        }
                    }
                }
                is MapState.Loading -> LoadingView(
                    loadingText = "Loading Map",
                    modifier = Modifier.fillMaxSize()
                )
                is MapState.Error -> ErrorView(
                    errorMessage = "Failed to load map",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    private fun loadMaps(context: Context, venue: Venue) {
        _mapState.value = MapState.Loading(0)

        TrackingService.createInstance(context).changeTrackingPayload(
            BreadcrumbPayload(
                selectedResort = venue.data.name
            )
        )

        map = Map(venue)
        scene = Scene(venue)

        map.load(
            context = context,
            onComplete = { map ->
                scene.load(
                    context = context,
                    onComplete = { scene ->
                        _mapState.value = MapState.Loaded(map, scene)
                    },
                    onProgress = { progress, total, downloaded ->
                    },
                    onError = {
                        _mapState.value = MapState.Error
                    }
                )
            },
            onProgress = { progress, total, downloaded ->
            },
            onError = {
                _mapState.value = MapState.Error
            }
        )
    }
}