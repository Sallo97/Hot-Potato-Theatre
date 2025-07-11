package org.example

import org.example.game.Game
import org.example.player.BarnumPlayer
import org.example.player.IrrationalPlayer
import org.example.player.Player
import org.example.player.RationalPlayer
import org.example.potato.Potato

fun main() {
    println("Welcome to Hot Potato Game!")
    val game = createGame()
    game.run()
    println("Bye Bye!")
}


/**
 * @return a Potato object with parameters coming from user's input.
 */
fun createHotPotato() : Potato{
    println("Creating the hot potato...")
    print("Lifetime = ")
    val lifetime = readln().toInt()

    print("Gain = ")
    val gain = readln().toInt()

    print("Loss = ")
    val loss = -readln().toInt()

    val potato = Potato(lifetime, gain, loss)
    println("Created the potato: $potato")
    return potato
}

/**
 * Creates a player among the available archetypes.
 * @return the player object with parameters coming from user's input
 */
fun createPlayer(id: Int = 0) : Player {
    println("Creating player $id...")
    println("Select which type of player do you want between:")
    println("1 - Rational: never takes the good")
    println("2 - Irrational: always takes the good")
    println("3 - Barnum: is aware of the possibility of irrational actors among the population")
    println("Input: ")
    val type = readln().toInt()

    val player = when (type) {
        1 -> RationalPlayer(id)
        2 -> IrrationalPlayer(id)
        3 -> {
            println("choose the probability that an alter can be another Barnum player (must be a value between 0 and 1: ")
            val prob = readln().toDouble()
            BarnumPlayer(id, prob)
            }
        else -> error("Invalid input")
    }

    println("Created the player: $player")
    return player
}

/**
 * @return the game object with parameters coming from user's input
 */
fun createGame() : Game {
    println("Creating game...")
    val potato = createHotPotato()

    print("Input number of players: ")
    val numOfPlayers = readln().toInt()
    val population = mutableSetOf<Player>()
    for (i in 1..numOfPlayers) {
        val newPlayer = createPlayer(i)
        population.add(newPlayer)
    }

    val game = Game(potato, population)
    return game
}