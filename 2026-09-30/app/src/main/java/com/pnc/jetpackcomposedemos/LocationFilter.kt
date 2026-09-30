package com.pnc.jetpackcomposedemos

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable

@Composable
fun LocationFilter(
    locationFilter: String,
    onLocationChange: (String) -> Unit
) {

    TextField(
        value = locationFilter,
        onValueChange = onLocationChange,
        label = {
            Text("Location:")
        }
    )

}