package frontend.ui

import backend.player.createSetOfPlayersFromStdin
import backend.game.Game
import backend.potato.createHotPotatoFromStdin
import kotlin.math.absoluteValue

const val invalidInputMsg = "Invalid input, retrying..."

/**
 * @return a Game Object with parameters coming from user's input
 */
fun createGameFromStdin() : Game {
    println("Creating game...")
    val potato = createHotPotatoFromStdin()
    val isHomogeneous = gameTypeFromStdin()

    val numOfPlayers = absIntFromStdin("number of players")
    val set = createSetOfPlayersFromStdin(numOfPlayers, isHomogeneous)

    val game = Game(potato, set)
    return game
}

/**
 * @param [question] the question asked the user for which it needs to respond.
 * @return true if the user choose "y", false if it chooses "n".
 */
fun choiceFromStdin(question: String) : Boolean {
    while(true) {
        print("$question Press Y/y for Yes, N/n for No: ")
        val choice = readln().lowercase()
        if (choice == "n" || choice == "y") {
            return choice == "y"
        }
        println(invalidInputMsg)
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
        println(invalidInputMsg)
    }
}

/**
 * @param [name] the name of the argument requested to Stdin.
 * @param [range] the range of integers in which the value must be inbetween (included)
 * @return a correct input from stdin parsed as an absolute integer.
 */
fun absIntWithinRangeFromStdin(name: String, range: IntRange) : Int {
    while(true){
        val input = absIntFromStdin(name)
        if(input in range) {
            return input
        } else {
            println(invalidInputMsg)
        }
    }
}
/**
 * @param [name] the name of the argument requested to Stdin.
 * @return a correct input from stdin parsed as an absolute double.
 */
fun absDoubleFromStdin(name:String) : Double {
    while(true) {
        print("Input $name (will take the absolute value): ")
        val input = readln().toDoubleOrNull()
        if(input != null) {
            return input.absoluteValue
        } else {
            println(invalidInputMsg)
        }
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
        input = absDoubleFromStdin(name)
        if(input in range.start.toDouble()..range.endInclusive.toDouble()) {
            return input.absoluteValue
        }
        println(invalidInputMsg)
    }
}

/**
 * @return true if the user asked for a Homogenous game, false otherwise.
 */
private fun gameTypeFromStdin() : Boolean{
    println("Select type of game:")
    println("1 - Homogeneous: game where players are all of the same type")
    println("2 - Mixed: game where players can be of different types")

    val choice = absIntWithinRangeFromStdin("type of game", 1..2)
    val result = choice == 1
    return result
}
