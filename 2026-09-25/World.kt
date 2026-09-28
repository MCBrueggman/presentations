package com.bootcamp.kotlinquest

// "object" instead of "class" here
// means that WorldFactory is a thread-safe singleton
object WorldFactory {

    const val CREW_QUARTERS = "quarters"
    const val STORAGE = "storage"
    const val ENGINEERING = "engineering"
    const val CONTROL_ROOM = "control"
    const val MEDICAL_BAY = "medical"
    const val ESCAPE_BAY = "escape"

    fun createRooms(): Map<String, Room> {
        // first, create items
        val wrench = Item(
            name = "wrench",
            description = "A heavy maintenance wrench used on station machinery",
            type = ItemType.TOOL,
            value = 50
        )

        val accessCard = Item(
            name = "Access Card",
            description = "A security card marked ESCAPE SYSTEMS.",
            type = ItemType.KEY,
            value = 75
        )

        val medicalKit = Item(
            name = "Medical Kit",
            description = "Restores up to 30 health points.",
            type = ItemType.MEDICAL,
            value = 30
        )

        val oxygenCanister = Item(
            name = "Oxygen Canister",
            description = "Restores up to 35 oxygen points.",
            type = ItemType.OXYGEN,
            value = 35
        )

        return listOf(
            Room(
                id = CREW_QUARTERS,
                name = "Crew Quarters",
                description = "Emergency lights reveal abandoned bunks and a sealed viewport.",
                exits = mapOf("east" to ENGINEERING, "south" to STORAGE)
            ),
            Room(
                id = STORAGE,
                name = "Storage Room",
                description = "Supply crates have broken loose and drift across the room.",
                exits = mapOf("north" to CREW_QUARTERS, "east" to CONTROL_ROOM),
                items = mutableListOf(wrench, oxygenCanister)
            ),
            Room(
                id = ENGINEERING,
                name = "Engineering Bay",
                description = "The oxygen regulator flashes red beside a damaged control panel.",
                exits = mapOf("west" to CREW_QUARTERS, "east" to CONTROL_ROOM)
            ),
            Room(
                id = CONTROL_ROOM,
                name = "Control Room",
                description = "Dark monitors surround the station command console.",
                exits = mapOf(
                    "west" to ENGINEERING,
                    "south" to STORAGE,
                    "east" to MEDICAL_BAY,
                    "north" to ESCAPE_BAY
                ),
                items = mutableListOf(accessCard)
            ),
            Room(
                id = MEDICAL_BAY,
                name = "Medical Bay",
                description = "Cabinets hang open above an emergency treatment table.",
                exits = mapOf("west" to CONTROL_ROOM),
                items = mutableListOf(medicalKit)
            ),
            Room(
                id = ESCAPE_BAY,
                name = "Escape Bay",
                description = "A single escape pod waits behind its launch console.",
                exits = mapOf("south" to CONTROL_ROOM)
            )
        ).associateBy { it.id }


    }

}