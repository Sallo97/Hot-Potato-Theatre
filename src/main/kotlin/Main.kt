package org.example

import org.example.game.Game
import org.example.player.BarnumPlayer
import org.example.player.GulliblePlayer
import org.example.player.Player
import org.example.player.RationalPlayer
import org.example.potato.Potato
import kotlin.math.absoluteValue

fun main() {
    println("Welcome to Hot Potato Game!")
    while (true) {
        val game = createGameFromStdin()
        game.run()

        val choice = choiceFromStdin("Wanna play another game?")
        if (choice == "n") {
            break
        }
    }
    println("Exiting, Bye Bye!")
}

/**
 * @param [question] the question asked the user for which it needs to respond.
 * @return either "y" or "n" depending on if the user agrees or not with [question]
 */
fun choiceFromStdin(question: String) : String {
    while(true) {
        print("$question Press Y/y for Yes, N/n for No: ")
        val choice = readln().lowercase()
        if (choice == "n" || choice == "y") {
            return choice
        }
        println("Invalid input, retrying...")
    }
}

/**
 * @param [name] the name of the argument requested to Stdin.
 * @return a correct input from stdin parsed as an absolute integer
 */
fun absIntFromStdin(name: String) : Int {
    var input: Int?
    while(true){
        print("Input $name (will take the absolute value): ")
        input = readln().toIntOrNull()
        if(input != null) {
            return input.absoluteValue
        }
        println("Invalid input, retrying...")
    }
}

/**
 * @return a Potato object with parameters coming from user's input.
 */
fun createHotPotatoFromStdin() : Potato{
    println("Creating the hot potato...")
    val lifetime = absIntFromStdin("lifetime").toUInt()
    val gain = absIntFromStdin("gain").toUInt()
    val loss = absIntFromStdin("loss").toUInt()
    val potato = Potato(lifetime, gain, loss)
    println("Created the potato: $potato")
    return potato
}

/**
 * @return the Player object with parameters coming from user's input
 */
fun createPlayerFromStdin(id: Int = 0) : Player {
    println("Creating player $id...")
    println("Select which type of player do you want between:")
    println("1 - Rational: never takes the good")
    println("2 - Gullible: always takes the good")
    println("3 - Barnum: is aware of the possibility of irrational actors among the population")
    var player: Player?
    while(true) {
        val type = absIntFromStdin("Player's type")
        when (type) {
            1 -> {
                player = RationalPlayer(id)
                break
            }
            2 -> {
                player = GulliblePlayer(id)
                break
            }
            3 -> {
                println("choose the probability that an alter can be another Barnum player (must be a value between 0 and 1: ")
                val prob = readln().toDouble()
                player = BarnumPlayer(id, prob)
                break
            }

            else -> {
                println("Inserted type: $type is not in the valid range, retrying...")
            }
        }
    }

    println("Created the player: $player")
    return player
}

/**
 * @return a Game object with parameters coming from user's input
 */
fun createGameFromStdin() : Game {
    println("Creating game...")
    val potato = createHotPotatoFromStdin()

    val numOfPlayers = absIntFromStdin("number of players")
    val population = mutableSetOf<Player>()
    for (i in 1..numOfPlayers) {
        val newPlayer = createPlayerFromStdin(i)
        population.add(newPlayer)
    }

    val game = Game(potato, population)
    return game
}