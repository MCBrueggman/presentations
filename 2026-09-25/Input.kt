package com.bootcamp.kotlinquest

fun getRequiredText(prompt: String): String {

    while (true) {
        print(prompt)
        val value = readln().trim()

        if (value.isNotEmpty()) return value
        println("Please enter a value.")
    }

}

fun readMenuChoice(validChoices: IntRange): Int {

    while (true) {
        print("Choice: ")

        try {
            val choice = readln().trim().toInt()

            if (choice in validChoices) return choice

            println("Choose a number from ${validChoices.first} to ${validChoices.last}")
        }
        catch (_: NumberFormatException) {
            println("Please enter a number.")
        }
    }

}



