package org.example.frontend

import org.example.backend.game.Game
import org.example.backend.player.BarnumPlayer
import org.example.backend.player.BenthamitePlayer
import org.example.backend.player.CoalitionalPlayer
import org.example.backend.player.DirectAltruistPlayer
import org.example.backend.player.GulliblePlayer
import org.example.backend.player.MyopicPlayer
import org.example.backend.player.Player
import org.example.backend.player.RationalPlayer
import org.example.backend.player.StochasticPlayer
import org.example.backend.potato.Potato
import kotlin.math.absoluteValue

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
    println("6 - Direct Altruist: concerned with the well-being of another player. For sake of simplicity we assume the beneficiary is always an alter that still has to get the hot potato.")
    println("7 - Benthamite: focused on maximizing the total payoff of the game. Still it is aware that other player could take the risk.")
    println("8 - Coalitional: concerned in forming a coalition with other players of the same type in order to get a better payoff.")

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
                val threshold = absIntFromStdin("threshold")

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
            6 -> {
                val altruismMessage = "altruism, value between [0,1] representing how much the player is willing to help the beneficiary."
                val altruism = absDoubleWithinRangeFromStdin(altruismMessage, IntRange(0, 1))

                val helpAlterMessage = "the belief of the current player that an alter will help the same beneficiary. Is a value\n" +
                        " * in [0,1]."
                val helpAlterBelief = absDoubleWithinRangeFromStdin(helpAlterMessage, IntRange(0, 1))

                player = DirectAltruistPlayer(id, altruism, helpAlterBelief)
                break
            }
            7 -> {
                val alterMessage = "the belief of the current player that an alter will accept the potato at the current turn. Is a value in [0,1]"
                val alterAcceptBelief = absDoubleWithinRangeFromStdin(alterMessage, IntRange(0, 1))

                player = BenthamitePlayer(id, alterAcceptBelief)
                break
            }
            8 -> {
                val riskMessage = "how much the current players is willing to risk accepting the potato for the proposed coalition. Is a value between 0 and 1."
                val acceptanceToRisk = absDoubleWithinRangeFromStdin(riskMessage, IntRange(0, 1))

                player = CoalitionalPlayer(id, acceptanceToRisk)
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
 * @return a Potato object with parameters coming from user's input.
 */
fun createHotPotatoFromStdin() : Potato{
    println("Creating the hot potato...")
    val lifetime = absIntFromStdin("lifetime")
    val gain = absIntFromStdin("gain")
    val loss = absIntFromStdin("loss")
    val potato = Potato(lifetime, gain, loss)
    println("Created the potato: $potato")
    return potato
}
