package com.bootcamp.kotlinquest

// extension method to let us easily find an item in the player's inventory
fun List<Item>.findByName(name: String): Item? =
    firstOrNull {
        it.name.equals(name.trim(), ignoreCase = true)
    }


// extension method to display an item
fun Item.displayLabel(): String = "$name (${type.name.lowercase()})"


// extension method for a status bar for player's stats
fun Int.toStatusBar(max: Int = 100, width: Int = 10): String {
    if (max <= 0 || width <= 0) return ""

    val filled = (this.coerceIn(0, max) * width / max)
    return "#".repeat(filled) + "-".repeat(width - filled)
}


