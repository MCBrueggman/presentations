package com.bootcamp.kotlinquest

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    displayEmergencySequence()

    val playerName = getRequiredText("Enter your astronaut name: ")
    val player = Player(playerName, currentRoomId = WorldFactory.CREW_QUARTERS)

    Game(
        player = player,
        rooms = WorldFactory.createRooms()
    ).run()
}

private suspend fun displayEmergencySequence() {
    coroutineScope {
        launch {
            listOf(
                "Emergency power online...",
                "Oxygen regulator failure detected...",
                "Escape route calculation complete..."
            ).forEach { message ->
                println(message)
                delay(1000)
            }
        }
    }
}



