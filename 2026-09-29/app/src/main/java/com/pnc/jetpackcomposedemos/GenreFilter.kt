package com.pnc.jetpackcomposedemos

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

@Composable
fun GenreFilter() {

    var filter by remember {
        mutableStateOf("")
    }

    TextField(
        value = filter,
        onValueChange = {
            filter = it
        },
        label = {
            Text("Genre:")
        }
    )


}
