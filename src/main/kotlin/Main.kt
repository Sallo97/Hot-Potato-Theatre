package org.example

import org.example.game.Game
import org.example.player.BarnumPlayer
import org.example.player.GulliblePlayer
import org.example.player.MyopicPlayer
import org.example.player.Player
import org.example.player.RationalPlayer
import org.example.player.StochasticPlayer
import org.example.potato.Potato
import kotlin.math.absoluteValue

fun main() {
    println("Welcome to Hot Potato Game!")
    while (true) {
        val game = createGameFromStdin()
        game.run()
        println(game.getEndGameInfo())

        val choice = choiceFromStdin("Wanna play another game?")
        if (choice == "n"  ) {
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
    while(true) {
        print("Input $name (will take the absolute value): ")
        input = readln().toIntOrNull()
        if(input != null) {
            return input.absoluteValue
        }
        println("Invalid input, retrying...")
    }
}

/**
 * @param [name] the name of the argument requested to Stdin.
 * @param [range] the range of integers in which the value must be inbetween (included)
 * @return a correct input from stdin parsed as an absolute double
 */
fun absDoubleWithinRangeFromStdin(name: String, range: IntRange) : Double {
    var input: Double?
    while(true) {
        print("Input $name (will take the absolute value): ")
        input = readln().toDoubleOrNull()
        if(input != null && range.contains(input.toInt())) {
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
    println("4 - Myopic: if the remaining turns are larger than its threshold it behaves gullably, otherwise rationally")
    println("5 - Stochastic: interprets the decision as a static stochastic process.")

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
                val message = "the probability that other alters are Barnum players"
                val prob = absDoubleWithinRangeFromStdin(message, IntRange(0, 1))
                player = BarnumPlayer(id, prob)
                break
            }
            4 -> {
                val threshold = absIntFromStdin("threshold").toUInt()
                player = MyopicPlayer(id, threshold)
                break
            }
            5 -> {
                val message = "weight, an hyperparameter between 0 and 1 which determines" +
                        " how much weight the probability have."
                val weight = absDoubleWithinRangeFromStdin(message, IntRange(0, 1))
                player = StochasticPlayer(id, weight)
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