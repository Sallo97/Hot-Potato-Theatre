import backend.player.PlayerType
import backend.examples.examples
import backend.game.createGameFromStdin
import frontend.ui.InputRestriction
import frontend.ui.intFromStdin
import frontend.ui.choiceFromStdin

fun main() {
    val greetings = "Welcome to the Simple Hot Potato Theatre!\n"
    println(greetings)

    chooseAndExecuteMode()

    println("Exiting, Bye Bye!")
}


/**
 * Handles choose and execution of mode.
 */
private fun chooseAndExecuteMode() {
    while (true) {
        val message = "Choose one of the following mode:\n" +
                "1 - Create and play a custom Hot Potato Game.\n" +
                "2 - Play one of the available examples.\n" +
                "3 - Find a longest chain for a game.\n" +
                "4 - Help.\n" +
                "5 - Exit application."
        println(message)

        val mode = intFromStdin("mode", InputRestriction.STRICTLY_POSITIVE, 1..5)
        println()
        when(mode) {
            1 -> {
                createAndExecuteGame()
            }
            2 -> {
                playExample()

            }
            3 -> {
                findOptimal()
            }
            4 -> {
                help()
            }
            5 -> {
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

/**
 * Handles the choose and execution of examples.
 */
private fun playExample() {
    while(true){
        println("Choose one of the available examples:")
        for ((index, example) in examples.withIndex()) {
            println("${index + 1} - $example")
        }

        val numOfExamples = examples.size
        val choice = intFromStdin("example", InputRestriction.STRICTLY_POSITIVE,  1..numOfExamples)
        val example = examples[choice - 1]
        example.run()
        println(example.gameToString())

        if (!choiceFromStdin("Wanna play another example?")) {
            break
        }
    }

}

/**
 * Finds a possible optimal solution for a game.
 */
private fun findOptimal() {
    while(true) {
        val game = createGameFromStdin()
        val solution = game.findLongestChain()

        val message = if (solution.isEmpty()) {
            "No solution was found for the game."
        } else {
            "Found the following solution:\n${solution}"
        }
        println(message)

        if (!choiceFromStdin("Wanna try to find another solution?")) {
            break
        }
    }
}

/**
 * Prints a description of the application
 */
private fun help() {
    val briefDescription = "This program offers way to create, analyse and play Simple Hot Potato Games (SHPG for short), offering" +
            "an assortment of customization option like different players' type, each with their own unique behaviour."
    println(briefDescription)

    while(true){
        val choiceMessage = "What you wanna know more about?\n" +
                "1 - What are hot potatoes and Simple Hot Potato Games?\n" +
                "2 - What types of players are available?\n" +
                "3 - What is a longest chain and how the program finds it?\n" +
                "4 - What are the available examples?"
        println(choiceMessage)
        val choice = intFromStdin("choice", InputRestriction.STRICTLY_POSITIVE, 1..4)
        when (choice) {
            1 -> {
                val shpgMsg =
                    "Simple Hot Potato Games (SHPG for short) are a mathematical formalisation of the classic game of" +
                            "the same name, in which a group of players try to exchange to each other a good -the hot potato- until" +
                            "it becomes worthless. The last player holding the hot potato is the loser of the game.\n\n" +
                            "The hot potato is formalized as an exchangeable good with:\n" +
                            "\t- lifetime: the number of turns for which it can still be passed around.\n" +
                            "\t- gain: the payoff received for taking the good to all players that arend the last one.\n" +
                            "\t- loss: the penalty received by the lsat player for taking the good.\n\n" +
                            "An SHPG is composed of:\n" +
                            "\t- potato: the unique hot potato of the game.\n" +
                            "\t- population: the set of players partaking the game.\n\n" +
                            "At the start the game searches for a player willing to take the good. If no one is willing, the game" +
                            "ends immediately with no winners or losers. If found, then the current player will try to find another" +
                            "player interested in taking the good. The game continues until either the potato's lifetime expired or" +
                            "there is no one willing to accept it anymore.\n" +
                            "At this point the payoff are distributed among the players, with the last player taking a loss while all" +
                            "the other who accept the potato will take the gain.\n\n" +
                            "It is possible to construct:\n" +
                            "Homogeneous games: games where all players are of the same type\n" +
                            "Mixed games: games where players can be of different types.\n" +
                            "Players are only described by their payoff -the value obtained by partaking the game (initially set to 0)-" +
                            "and their decision strategy when they are proposed to take the good. Different player's type define" +
                            "different decision strategies.\n\n"
                print(shpgMsg)
            }

            2 -> {
                val typeMsg = "Players in the Simple Hot Potato Game (SHPG for short) have only one behavior: " +
                        "decide to either accept or deny to take the hot potato when offered\n\n" +
                        "The decision logics is embedded to the associated type of the player.\n" +
                        "The types available are:\n${PlayerType.getAvailableTypes()}\n\n"
                print(typeMsg)
            }

            3 -> {
                val optimalMsg = "In a Simple Hot Potato Game (SHPG), a longest chain is defined as a longest ordered list" +
                        "of players such that, if the game proceeds by selecting the next player from this list at each " +
                        "turn, each player will accept the potato.\n\n" +
                        "The algorithm for finding an optimal solution works as follows:\n" +
                        "1 - For each player in the SHPG, we identify at each turn whether they will accept the potato or not.\n" +
                        "2 - At each turn, the algorithm checks which players are willing to accept the potato.\n" +
                        "If no such player exists, the game has no optimal solution and the algorithm terminates.\n" +
                        "Otherwise, it selects as a candidate the player among them with the fewest remaining turns in " +
                        "which they are still willing to accept the potato.\n\n" +
                        "Note: Coalitional players act as a single unit, since a member's willingness to accept the " +
                        "potato depends on the previous member's acceptance.\n" +
                        "Their acceptance is therefore evaluated collectively.\n" +
                        "If the algorithm selects the first player in a coalition, the entire coalition must be selected as a group.\n\n"
                print(optimalMsg)
            }
            4 -> {
                val examplesMsg =
                    "The available examples are an assortment of ready-to-play games to show the possible scenarios" +
                            "realizable with this program.\n\n"
                print(examplesMsg)
            }
        }
        if (!choiceFromStdin("Wanna know more about the program?")) {
            break
        }
    }
}











