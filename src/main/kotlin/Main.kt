package org.example

import org.example.frontend.absIntWithinRangeFromStdin
import org.example.frontend.choiceFromStdin
import org.example.frontend.createGameFromStdin

fun main() {
    val greetings = "Welcome to the Simple Hot Potato Theatre!\n"
    println(greetings)

    chooseAndExecuteMode()

    println("Exiting, Bye Bye!")
}

private fun Help() {
    TODO("I'M WORKING ON IT I'M WORKING!")
}

private fun playExample() {
    TODO("I'M WORKING ON IT I'M WORKING!")
}

/**
 * Handles choose and execution of mode.
 */
private fun chooseAndExecuteMode() {
    while (true) {
        val message = "Choose one of the following mode:\n" +
                "1 - Create and play a custom Hot Potato Game.\n" +
                "2 - Play one of the available examples.\n" +
                "3 - Help.\n" +
                "4 - Exit application."
        println(message)
        val mode = absIntWithinRangeFromStdin("mode", 1..4)
        when(mode) {
            1 -> {
                createAndExecuteGame()
            }
            2 -> {
                playExample()

            }
            3 -> {
                Help()
            }
            4 -> {
                break
            }
        }
    }
}

/**
 * Handles creation and execution of a game.
 */
private fun createAndExecuteGame() {
    while(true) {

        val game = createGameFromStdin()
        game.run()
        println(game.toString())

        if (!choiceFromStdin("Wanna play another game?")) {
            break
        }
    }
}











