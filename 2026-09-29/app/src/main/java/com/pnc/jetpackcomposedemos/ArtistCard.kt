package com.pnc.jetpackcomposedemos

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pnc.jetpackcomposedemos.ui.theme.JetpackComposeDemosTheme

@Composable
fun ArtistCard(
    artist: Artist,
    onClick: () -> Unit
) {
    Log.d("ArtistCard", "Recomposing ArtistCard for ${artist.name}")
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = artist.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text("Genre: ${artist.genre}")
            Text("Location: ${artist.location}")
        }
    }

}


@Preview(showBackground = false)
@Composable
fun ArtistCardPreview() {
    val artist = Artist(
        id = 1,
        name = "Artist Name",
        genre = "Genre",
        location = "Location",
        imageUrl = "",
        description = "",
        tags = ""
    )

    JetpackComposeDemosTheme {
        ArtistCard(
            artist = artist,
            onClick = {}
        )
    }
}

