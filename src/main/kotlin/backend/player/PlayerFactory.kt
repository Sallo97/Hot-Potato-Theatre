package backend.player

import frontend.ui.absDoubleFromStdin
import frontend.ui.absDoubleWithinRangeFromStdin
import frontend.ui.absIntFromStdin
import frontend.ui.absIntWithinRangeFromStdin

/**
 * @return a set of [numOfPlayers] players. If [isHomogeneous] is passed, then the set will have only player of the same
 * type, otherwise each player could have different type.
 */
fun createSetOfPlayersFromStdin(numOfPlayers: Int, isHomogeneous: Boolean = false) : Set<Player> {
    // println("Creating set of players...")

    var type: Int? = if(isHomogeneous) {
        getPlayerTypeFromStdin()
    } else {
        null
    }

    val result:MutableSet<Player> = mutableSetOf()
    for (id in 1..numOfPlayers) {
        if(!isHomogeneous) {
            type = getPlayerTypeFromStdin()
        }
        val player = createPlayerOfTypeFromStdin(type!!, id)
        result.add(player)
    }
    return result
}

/**
 * @param [id] the unique identifier of the player.
 * @return a player with arguments determined by the user from stdin.
 */
fun createPlayerFromStdin (id:Int = 0) : Player {
    val type = getPlayerTypeFromStdin()
    val player = createPlayerOfTypeFromStdin(type, id)
    return player
}

/**
 * @return player object of specified [type] and [id] with parameters coming from user's input.
 */
private fun createPlayerOfTypeFromStdin(type: Int, id:Int = 0) : Player {
    val playerType = PlayerType.fromInt(type)
    val player = when(playerType) {
         PlayerType.RATIONAL -> {
            RationalPlayer(id)
        }
        PlayerType.GULLIBLE -> {
            GulliblePlayer(id)
        }
        PlayerType.BARNUM -> {
            val message = "the probability that other alters are Barnum players"
            val prob = absDoubleWithinRangeFromStdin(message, IntRange(0, 1))
            BarnumPlayer(id, prob)
        }
        PlayerType.MYOPIC -> {
            val message = "threshold"
            val threshold = absIntFromStdin(message)
            MyopicPlayer(id, threshold)
        }
        PlayerType.STOCHASTIC -> {
            val message = "the probability that an alter will deny the good at the next turn"
            val alterDenyBelief = absDoubleWithinRangeFromStdin(message, IntRange(0, 1))
            StochasticPlayer(id, alterDenyBelief)
        }
        PlayerType.DIRECT_ALTRUIST -> {
            val altruismMessage = "altruism, value between [0,1] representing how much the player is willing to help the beneficiary."
            val altruism = absDoubleWithinRangeFromStdin(altruismMessage, IntRange(0, 1))

            val alterBelief = "the belief of the current player that an alter will help the same beneficiary. Is a value > 0"
            val helpAlterBelief = absDoubleFromStdin(alterBelief)

            DirectAltruistPlayer(id, altruism, helpAlterBelief)
        }
        PlayerType.BENTHAMITE -> {
            val gainMessage = "how much weight has the acceptance of the hot potato. Is a value > 0"
            val gainWeight = absDoubleFromStdin(gainMessage)

            val lossMessage = "how much weight has the loss of the hot potato. Is a value > 0"
            val lossWeight = absDoubleFromStdin(lossMessage)

            BenthamitePlayer(id, gainWeight, lossWeight)
        }
        PlayerType.COALITIONAL -> {
            val riskMessage = "how much the current players is willing to risk accepting the potato for the proposed coalition. Is a value between 0 and 1."
            val acceptanceToRisk = absDoubleWithinRangeFromStdin(riskMessage, IntRange(0, 1))

            CoalitionalPlayer(id, acceptanceToRisk)
        }
        PlayerType.INPUT -> {
            InputPlayer(id)
        }

        else -> {
            error("Invalid type for player")
        }
    }
    // println("Created the player: $player")
    return player
}

/**
 * @return the player's type as an integer
 */
private fun getPlayerTypeFromStdin() : Int {
    val typeOfPlayerMsg = "Select which type of player do you want between:"
    println(typeOfPlayerMsg)
    println(PlayerType.getAvailableTypes())

    val type : Int = absIntWithinRangeFromStdin(
        "player type",
        1..PlayerType.entries.size
    )
    return type
}