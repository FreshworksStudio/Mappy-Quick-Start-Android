package io.mappy.mappysdkimplementationexample

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.mappy.mappysdkimplementationexample.java.activities.VenuesActivity
import io.mappy.mappysdkimplementationexample.kotlin.activities.VenuesActivityKt

@ExperimentalFoundationApi
class InitialActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ChooseLanguageView()
        }
    }

    @Composable
    fun ChooseLanguageView() {
        Surface(
            modifier = Modifier.background(color = Color.White),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Please choose the implementation you want to test",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "Kotlin implementation has been made with Jetpack Compose UI and Kotlin Coroutines (Those are very powerful but exists only in kotlin)",
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(5.dp))
                Text(
                    text = "Java implementation has been made with old XML UI and Callbacks",
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(20.dp))
                Button(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    onClick = ::openKotlinExample
                ) {
                    Text(text = "Kotlin Implementation")
                }
                Spacer(modifier = Modifier.size(5.dp))
                Button(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    onClick = ::openJavaExample
                ) {
                    Text(text = "Java Implementation")
                }
            }
        }
    }

    private fun openKotlinExample() {
        startActivity(Intent(this, VenuesActivityKt::class.java))
    }

    private fun openJavaExample() {
        startActivity(Intent(this, VenuesActivity::class.java))
    }

    @Preview(device = Devices.PIXEL_3_XL)
    @Composable
    fun Preview() {
        ChooseLanguageView()
    }

}