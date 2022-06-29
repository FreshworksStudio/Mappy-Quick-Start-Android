package io.mappy.mappysdkimplementationexample.kotlin.activities

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.bemappy.sdk.models.Map
import io.bemappy.sdk.models.Scene
import io.bemappy.sdk.models.Venue
import io.bemappy.sdk.ui.compose.MapView
import io.bemappy.sdk.ui.compose.SceneView
import io.bemappy.sdk.ui.compose.controllers.MapController
import io.bemappy.sdk.ui.compose.controllers.SceneController
import io.mappy.mappysdkimplementationexample.kotlin.views.ErrorView
import io.mappy.mappysdkimplementationexample.kotlin.views.LoadingView
import java.lang.NullPointerException

@ExperimentalFoundationApi
class MapActivityKt: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val venue = intent.getParcelableExtra<Venue>("venue") ?: throw NullPointerException()

        setContent {
            Surface( modifier = Modifier.background(Color.White)) {
                Body(venue)
            }
        }
    }

    @Composable
    fun Body(venue: Venue) {
        val is3d = remember { mutableStateOf(false) }

        val mapController = remember { MapController() }
        val sceneController = remember { SceneController() }
        
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            val mapModifier = if (is3d.value) Modifier.size(0.dp) else Modifier
                .fillMaxSize()
                .background(Color.White)

            SceneView(
                scene = Scene(venue),
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                sceneController = sceneController,
                loadingView = { LoadingView(loadingText = "Loading 3D Map") },
                errorView = { ErrorView(errorMessage = "Failure Loading 3D Map")  }
            )

            MapView(
                map = Map(venue),
                modifier = mapModifier,
                mapController = mapController,
                loadingView = { LoadingView(loadingText = "Loading 2D Map", modifier = mapModifier) },
                errorView = { ErrorView(errorMessage = "Failure Loading 2D Map", modifier = mapModifier)  }
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.TopStart
            ) {
                IconButton(onClick = {  },
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
                        .size(50.dp)
                        .background(Color.White, shape = CircleShape)
                ) {
                    val text = if(is3d.value) "3D" else "2D"
                    Text(text = text, fontSize = 8.sp)
                }
                Spacer(modifier = Modifier.size(5.dp))
                Button(
                    onClick = {
                        if (is3d.value) {
                            sceneController.zoomToDefault()
                        } else {
                            mapController.zoomToDefault()
                        }
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .background(Color.White)
                ) {
                    Text(text = "zoom", fontSize = 8.sp)
                }
                Spacer(modifier = Modifier.size(5.dp))
                Button(
                    onClick = {
                        if (is3d.value) {
                            sceneController.rotateToDefault()
                        } else {
                            mapController.rotateToDefault()
                        }
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .background(Color.White)
                ) {
                    Text(text = "rotate", fontSize = 8.sp)
                }
            }
        }
    }

}