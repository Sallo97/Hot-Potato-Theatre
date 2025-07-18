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
    fun run() {
        println("Executing example $name...")
        game.run()

    }

    override fun toString(): String {
        val message = "*** EXAMPLE NAME: $name ***\n" +
            "Description: $description"
        return message
    }
}