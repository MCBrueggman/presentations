package com.bootcamp.kotlinquest


enum class ItemType {
    TOOL,
    KEY,
    MEDICAL,
    OXYGEN
}

data class Item(
    val name: String,
    val description: String,
    val type: ItemType,
    val value: Int = 0
)

data class Room(
    val id: String,
    val name: String,
    val description: String,
    val exits: Map<String, String>,
    val items: MutableList<Item> = mutableListOf()
)

class Player(
    val name: String,
    var health: Int = 100,
    var oxygen: Int = 100,
    var currentRoomId: String
) {
    val inventory = mutableListOf<Item>()
    val visitedRooms = mutableSetOf<String>()       // stores ids of rooms the player visited
    var score = 0

    fun takeDamage(amount: Int) {
        health = (health - amount).coerceAtLeast(0)
    }

    fun restoreHealth(amount: Int) {
        health = (health + amount).coerceAtMost(100)
    }

    fun useOxygen(amount: Int) {
        oxygen = (oxygen - amount).coerceAtLeast(0)
    }

    fun restoreOxygen(amount: Int) {
        oxygen = (oxygen + amount).coerceAtMost(100)
    }

    fun isAlive(): Boolean = health > 0 && oxygen > 0
}


data class Reward<T> (
    val value: T,
    val points: Int
)

sealed class ActionResult {
    abstract val message: String

    data class Success(override val message: String): ActionResult()
    data class Failure(override val message: String): ActionResult()
    data class ItemFound(
        override val message: String,
        val reward: Reward<Item>
    ): ActionResult()
}

sealed class GameState {
    data object Playing: GameState()
    data class Won(val score: Int): GameState()
    data class Lost(val reason: String): GameState()
    data object Quit: GameState()
}


class StationEvent (
    val description: String,
    val effect: (Player) -> String
)












