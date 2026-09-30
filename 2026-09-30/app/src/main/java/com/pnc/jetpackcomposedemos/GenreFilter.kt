package com.pnc.jetpackcomposedemos

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable

@Composable
fun GenreFilter(
    genreFilter: String,
    onGenreChange: (String) -> Unit
) {

    TextField(
        value = genreFilter,
        onValueChange = onGenreChange,
        label = {
            Text("Genre:")
        }
    )

}

