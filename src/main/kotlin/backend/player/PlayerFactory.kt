package backend.player

import backend.game.GameType
import frontend.ui.InputRestriction
import frontend.ui.doubleFromStdin
import frontend.ui.intFromStdin

/**
 * @return a set of [numOfPlayers] players. [gameType] specifies the type of game
 * type, otherwise each player could have different type.
 */
fun createSetOfPlayersFromStdin(numOfPlayers: Int, gameType: GameType) : Set<Player> {
    // println("Creating set of players...")

    var type: PlayerType? = if(gameType == GameType.HOMOGENEOUS) {
        getPlayerTypeFromStdin()
    } else {
        null
    }

    val result:MutableSet<Player> = mutableSetOf()
    for (id in 1..numOfPlayers) {
        if(gameType == GameType.MIXED) {
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
private fun createPlayerOfTypeFromStdin(type: PlayerType, id:Int = 0) : Player {
    val player = when(type) {
         PlayerType.RATIONAL -> {
            RationalPlayer(id)
        }
        PlayerType.GULLIBLE -> {
            GulliblePlayer(id)
        }
        PlayerType.BARNUM -> {
            val message = "the probability that other alters are Barnum players"
            val prob = doubleFromStdin(message, InputRestriction.NOT_NEGATIVE, 0.0..1.0)
            BarnumPlayer(id, prob)
        }
        PlayerType.MYOPIC -> {
            val message = "threshold"
            val threshold = intFromStdin(message, InputRestriction.NOT_NEGATIVE)
            MyopicPlayer(id, threshold)
        }
        PlayerType.STOCHASTIC -> {
            val message = "the probability that an alter will deny the good at the next turn"
            val alterDenyBelief = doubleFromStdin(message, InputRestriction.NOT_NEGATIVE,  0.0..1.0)
            StochasticPlayer(id, alterDenyBelief)
        }
        PlayerType.DIRECT_ALTRUIST -> {
            val altruismMessage = "how much the player is willing to help the beneficiary."
            val altruism = doubleFromStdin(altruismMessage, InputRestriction.NOT_NEGATIVE, 0.0..1.0)

            val alterBelief = "the belief of the current player that an alter will help the same beneficiary"
            val helpAlterBelief = doubleFromStdin(alterBelief, InputRestriction.STRICTLY_POSITIVE)

            DirectAltruistPlayer(id, altruism, helpAlterBelief)
        }
        PlayerType.BENTHAMITE -> {
            val gainMessage = "how much weight has the acceptance of the hot potato"
            val gainWeight = doubleFromStdin(gainMessage, InputRestriction.NOT_NEGATIVE, range = 0.0..1.0)

            BenthamitePlayer(id, gainWeight)
        }
        PlayerType.COALITIONAL -> {
            // val riskMessage = "how much the current players is willing to risk accepting the potato for the proposed coalition"
            // val acceptanceToRisk = doubleFromStdin(riskMessage, InputRestriction.NOT_NEGATIVE, 0.0..1.0)

            CoalitionalPlayer(id)
        }
        PlayerType.INPUT -> {
            InputPlayer(id)
        }
    }
    // println("Created the player: $player")
    return player
}

/**
 * @return the player's type as an integer
 */
private fun getPlayerTypeFromStdin() : PlayerType {
    val typeOfPlayerMsg = "Select which type of player do you want between:"
    println(typeOfPlayerMsg)
    println(PlayerType.getAvailableTypes())

    val type : Int = intFromStdin(
        "player type",
        InputRestriction.NOT_NEGATIVE,
        1..PlayerType.entries.size
    )
    val result = PlayerType.fromInt(type)!!
    return result
}