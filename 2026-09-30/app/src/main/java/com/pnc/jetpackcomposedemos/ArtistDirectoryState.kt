package com.pnc.jetpackcomposedemos

data class ArtistDirectoryState(
    val artists: List<Artist> = emptyList(),
    val genreFilter: String = "",
    val locationFilter: String = "",
    val showFilter: Boolean = false,
    val selectedArtistId: Int? = null
) {
    val displayedArtists: List<Artist>
        get() = artists.filter {
            val matchesGenre = genreFilter == "" || it.genre.contains(genreFilter, ignoreCase = true)
            val matchesLocation = locationFilter == "" || it.location.contains(locationFilter, ignoreCase = true)

            matchesGenre && matchesLocation
        }

    val selectedArtist: Artist?
        get() = artists.find { it.id == selectedArtistId }

}




