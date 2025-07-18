package org.example.backend.examples

import org.example.backend.game.Game

/**
 * Represents a pre-cooked game. Concretely Wraps an already-created game with some
 * description about it.
 *
 * @property [game] the game offered.
 * @property [name] the name of the example.
 * @property [description] brief description of the game.
 */
class GameExample (
    private val game: Game,
    val name: String,
    val description: String
) {

    /**
     * Execute the game within the example.
     */
    fun run() {
        println("Executing example $name...")
        game.run()

    }

    /**
     * @return the string associated with the game within
     */
    fun gameToString() : String {
        return game.toString()
    }

    override fun toString(): String {
        val message = "example name: $name\n" +
            "description: $description"
        return message
    }
}