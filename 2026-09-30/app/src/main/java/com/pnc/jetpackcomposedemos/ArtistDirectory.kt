package com.pnc.jetpackcomposedemos

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ArtistDirectory(
    artists: List<Artist>,
    onArtistSelected: (Int) -> Unit,
    onBack: () -> Unit,
    useTwoPaneLayout: Boolean
) {
    var state by remember {
        mutableStateOf(ArtistDirectoryState(artists = artists))
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text("Available Artists")
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Return to dashboard"
                            )
                        }
                    }
                )
                Text(
                    text = "Displaying ${state.displayedArtists.size} artists",
                    modifier = Modifier.padding(0.dp, 12.dp)
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Show filter") },
                icon = {
                    Icon(Icons.Filled.Filter, contentDescription = "Show filter")
                },
                onClick = {
                    state = state.copy(showFilter = true)
                }
            )
        }
    ) { innerPadding ->

        if (useTwoPaneLayout) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    ArtistList(
                        artists = state.displayedArtists,
                        onArtistSelected = { artistId ->
                            state = state.copy(selectedArtistId = artistId)
                        },
                        innerPadding = PaddingValues(0.dp)
                    )
                }
                VerticalDivider()
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Selected artist")

                    AnimatedVisibility(
                        visible = state.selectedArtist != null,
                        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
                    ) {
                        ArtistDetails(
                            artist = state.selectedArtist!!
                        )
                    }

                }
            }

        } else {
            ArtistList(
                artists = state.displayedArtists,
                onArtistSelected = onArtistSelected,
                innerPadding = innerPadding
            )
        }


        if (state.showFilter) {
            ModalBottomSheet(
                onDismissRequest = {
                    state = state.copy(showFilter = false)
                }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Filter Artists",
                        style = MaterialTheme.typography.titleSmall
                    )
                    GenreFilter(
                        genreFilter = state.genreFilter,
                        onGenreChange = {
                            state = state.copy(genreFilter = it)
                        }
                    )
                    LocationFilter(
                        locationFilter = state.locationFilter,
                        onLocationChange = {
                            state = state.copy(locationFilter = it)
                        }
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                state = state.copy(genreFilter = "", locationFilter = "")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        ) {
                            Text("Clear")
                        }

                        Button(
                            onClick = {
                                state = state.copy(showFilter = false)
                            }
                        ) {
                            Text("Close")
                        }
                    }


                }
            }
        }

    }

}


@Preview(
    name = "Compact View",
    widthDp = 390,
    heightDp = 844
)
@Composable
fun CompactArtistDirectoryPreview() {
    ArtistDirectory(
        artists = Artist.getArtists(),
        onArtistSelected = {},
        onBack = {},
        useTwoPaneLayout = false,
    )
}

@Preview(
    name = "Expanded View",
    widthDp = 900,
    heightDp = 844
)
@Composable
fun ExpandedArtistDirectoryPreview() {
    ArtistDirectory(
        artists = Artist.getArtists(),
        onArtistSelected = {},
        onBack = {},
        useTwoPaneLayout = true,
    )
}






