package io.mappy.mappysdkimplementationexample.kotlin.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ErrorView(errorMessage: String, modifier: Modifier = Modifier, onReload: (() -> Unit)? = null) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = errorMessage)
        onReload?.let {
            Button(onClick = { onReload.invoke() }) {
                Text(text = "Reload")
            }
        }
    }
}