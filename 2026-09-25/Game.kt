package com.bootcamp.kotlinquest

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.random.Random

class Game (
    private val player: Player,
    private val rooms: Map<String, Room>
) {
    private var oxygenSystemRepaired = false
    private val completedObjectives = mutableSetOf<String>()
    private var state: GameState = GameState.Playing

    private val stationEvents = listOf(
        StationEvent(
            "You find a sealed emergency ration pack.",
            { target ->
                target.score += 20
                "You gain 20 points"
            }
        ),
        StationEvent(
            "An emergency oxygen valve release a small reserve",
            { target ->
                target.restoreOxygen(10)
                "You recover 10 oxygen"
            }
        ),
        StationEvent(
            "A damaged power cable snaps overhead.",
            { target ->
                val damage = Random.Default.nextInt(5, 13)
                target.takeDamage(damage)
                "The cable shocks you. You lose $damage health"
            }
        )
    )


    suspend fun run() {
        displayMission()

        while (state is GameState.Playing) {
            val room = currentRoom()
            player.visitedRooms.add(room.id)
            displayStatus(room)

            val consumesTurn = when (showMainMenu()) {
                1 -> handleResult(searchRoom(room))
                2 -> movePlayer(room)
                3 -> useItem()
                4 -> performRoomAction(room)
                5 -> {
                    displayInventory()
                    false
                }
                6 -> {
                    state = GameState.Quit
                    false
                }
                else -> false
            }

            if (consumesTurn && state is GameState.Playing) {
                processStationSystems()
                updateLossState()
            }

        }
        displayGameState(state)
    }

    private fun currentRoom(): Room =
        rooms[player.currentRoomId] ?: error("Unknown room: ${player.currentRoomId}")

    private fun movePlayer(room: Room) : Boolean {
        val exits = room.exits.entries.toList()
        println("\nAvailable exits:")

        exits.forEachIndexed { index, exit ->
            val destination = rooms[exit.value]?.name ?: "Unknown"

            println("${index + 1}. ${exit.key.replaceFirstChar { it.uppercase() }} to $destination")
        }

        val choice = readMenuChoice(1..exits.size)
        val destinationId = exits[choice - 1].value

        // player must have the Access Card item to move to the Escape Bay
        if (destinationId == WorldFactory.ESCAPE_BAY &&
            player.inventory.findByName("Access Card") == null) {
            println("\nThe Escape Bay door requires an access card.")
            return true
        }

        player.currentRoomId = destinationId
        println("\nYou move to ${currentRoom().name}")

        return true
    }

    private fun searchRoom(room: Room): ActionResult {
        val item = room.items.firstOrNull()
            ?: return ActionResult.Failure("You find nothing useful.")

        room.items.remove(item)
        return ActionResult.ItemFound(
            "You found a ${item.displayLabel()}.",
            Reward(item, item.value)
        )
    }

    private fun handleResult(result: ActionResult): Boolean {
        println("\n${result.message}")

        when (result) {
            is ActionResult.Success -> Unit
            is ActionResult.Failure -> Unit
            is ActionResult.ItemFound -> {
                player.inventory.add(result.reward.value)
                player.score += result.reward.points
                println("You gain ${result.reward.points} points.")
            }
        }
        return true
    }

    private fun displayInventory() {
        println("\n--- Inventory ---")
        if (player.inventory.isEmpty()) {
            println("Empty")
            return
        }

        player.inventory.forEach { item ->
            println("- ${item.displayLabel()}: ${item.description}")
        }

        // display the total value of the inventory
        val totalValue = player.inventory
            .map { it.value }
            .fold(0) { total, value -> total + value }
        println("Total value: $totalValue")
    }

    private fun useItem(): Boolean {
        val usableItems = player.inventory.filter {
            it.type == ItemType.MEDICAL || it.type == ItemType.OXYGEN
        }

        if (usableItems.isEmpty()) {
            println("\nYou have no usable supplies.")
            return false
        }

        println("\nUsable items:")
        usableItems.forEachIndexed { index, item ->
            println("${index + 1}. ${item.displayLabel()}")
        }

        val choice = readMenuChoice(1..usableItems.size)
        val item = usableItems[choice - 1]
        when (item.type) {
            ItemType.MEDICAL -> {
                player.restoreHealth(item.value)
                println("You use ${item.name} and restore ${item.value} health.")
            }
            ItemType.OXYGEN -> {
                player.restoreOxygen(item.value)
                println("You use ${item.name} and restore ${item.value} oxygen.")
            }
            else -> return false
        }

        player.inventory.remove(item)
        return true
    }

    private fun performRoomAction(room: Room): Boolean = when (room.id) {
        WorldFactory.ENGINEERING -> repairOxygenSystem()
        WorldFactory.ESCAPE_BAY -> launchEscapePod()
        else -> {
            println("\nThere is no special action to perform here.")
            false
        }
    }

    private fun repairOxygenSystem(): Boolean {
        if (oxygenSystemRepaired) {
            println("\nThe oxygen system is already stable.")
            return false
        }

        val wrench = player.inventory.findByName("Wrench")
        if (wrench == null) {
            println("\nThe regulator housing is stuck. You need a wrench.")
            return true
        }

        oxygenSystemRepaired = true
        completedObjectives.add("Repair oxygen system")

        player.restoreOxygen(30)
        player.score += 150

        println("\nYou repair the oxygen regulator. Oxygen loss is now slower.")
        println("You restore 30 oxygen and gain 150 points.")
        return true
    }

    private fun launchEscapePod(): Boolean {
        if (!oxygenSystemRepaired) {
            println("\nThe launch computer reports unsave station pressure.")
            println("Repair the oxygen system before launching.")
            return false
        }

        val accessCard = player.inventory.findByName("Access Card")
        if (accessCard == null) {
            println("\nThe launch console requires an access card.")
            return false
        }

        completedObjectives.add("Reach the escape pod")

        val explorationBonus = player.visitedRooms.size * 25
        val inventoryBonus = player.inventory.sumOf { it.value }

        val finalScore = player.score + explorationBonus + inventoryBonus

        state = GameState.Won(finalScore)

        return false
    }

    private fun displayMission() {
        println(
            """
                ===================================================
                            Kotlin Quest: Station Escape
                ===================================================
                ${player.name}, the station is failing.
                
                Mission:
                1. Find a wrench
                2. Repair the oxygen system in Engineering
                3. Find the escape-system access card
                4. Reach the Escape Bay and launch the pod.
                ===================================================
            """.trimIndent()
        )
    }

    private fun displayStatus(room: Room) {
        println(
            """
                ===================================================
                ${room.name.uppercase()}
                ${room.description}
                
                Health: [${player.health.toStatusBar()}] ${player.health}
                Oxygen: [${player.oxygen.toStatusBar()}] ${player.oxygen}
                Score: ${player.score}
                Objectives: ${completedObjectives.size}/2
                ===================================================
            """.trimIndent()
        )
    }

    private fun displayGameState(state: GameState) {
        when (state) {
            is GameState.Playing -> println("The mission continues...")
            is GameState.Won -> println("You escaped! Score: ${state.score}")
            is GameState.Lost -> println("Mission failed. ${state.reason}")
            is GameState.Quit -> println("Mission aborted. Loser.")
        }
    }

    private fun showMainMenu(): Int {
        println(
            """
                What would you like to do?
                1. Search the room
                2. Move 
                3. Use an item
                4. Perform a room action
                5. View inventory
                6. Quit
            """.trimIndent()
        )
        return readMenuChoice(1..6)
    }

    private suspend fun processStationSystems() = coroutineScope {

        val oxygenUsed = async {
            delay(120)
            if (oxygenSystemRepaired) 2 else 5
        }

        val event = async {
            delay(80)
            if (Random.Default.nextInt(100) < 35) stationEvents.random() else null
        }

        player.useOxygen(oxygenUsed.await())

        val stationEvent = event.await()
        if (stationEvent != null) {
            println("\nStation event: ${stationEvent.description}")
            println(stationEvent.effect(player))
        }
    }

    private fun updateLossState() {
        state = when {
            player.health <= 0 -> GameState.Lost("Your injuries are too severe.")
            player.oxygen <= 0 -> GameState.Lost("The station has run out of breathable air.")
            else -> GameState.Playing
        }
    }

}



