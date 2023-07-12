package io.mappy.mappysdkimplementationexample.kotlin.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.bemappy.sdk.models.Venue
import io.bemappy.sdk.services.auth.Mappy
import io.bemappy.sdk.services.venue.VenueService
import io.mappy.mappysdkimplementationexample.R
import io.mappy.mappysdkimplementationexample.kotlin.views.LoadingView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
class VenuesActivityKt : AppCompatActivity() {

    private val mappy by lazy {
        Mappy.createInstance(this)
    }

    private val venueService by lazy {
        VenueService.createInstance(this)
    }

    private val venues = mutableStateOf(emptyList<Venue>())

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Scaffold(modifier = Modifier.background(Color.White))
            {
                Body()
            }
        }

        getVenues()
    }

    private fun getVenues() {
        CoroutineScope(Dispatchers.IO).launch {
            kotlin.runCatching {
                mappy.initialize(
                    getString(R.string.client_id),
                    getString(R.string.client_secret)
                )
                venueService.getVenues()
            }.onSuccess {
                venues.value = it
            }.onFailure {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(this@VenuesActivityKt, it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openMap(venue: Venue) {
        startActivity(Intent(this, MapActivityKt::class.java).apply {
            putExtra("venue", venue)
        })
    }

    @Composable
    fun Body() {
        if (venues.value.isEmpty()) {
            LoadingView(loadingText = "Loading Venues", modifier = Modifier.fillMaxSize())
        } else {
            LazyColumn {
                items(
                    items = venues.value,
                    itemContent = { Venue(venue = it) }
                )
            }
        }
    }

    @Composable
    fun Venue(venue: Venue) {
        Card(
            modifier = Modifier.padding(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = venue.data.name,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(text = venue.data.description)
                Spacer(modifier = Modifier.size(10.dp))
                Button(
                    onClick = { openMap(venue) }
                ) {
                    Text(text = "Open Map")
                }
            }
        }
    }


}