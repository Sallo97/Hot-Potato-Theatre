package org.example.gameStatus

import org.example.player.Player

/**
 * Contains useful information regarding the status of a SHPG
 * @property [activePopulation] the non-ordered subset of players which at the current turn never got the good.
 * @property [chain] the ordered set of players which at the current turn already got the good.
 * @property [turn] the current turn of the game.
 * @property [numOfPlayers] the total number of players in the game.
 * @property [totalPayoff] sum of the payoff of all players at the current turn.
 *
 */
data class GameStatus(
    val activePopulation: MutableSet<Player>,
) {
    val chain: MutableList<Player> = mutableListOf()
    var turn: Int = 0
    val numOfPlayers: Int = activePopulation.size
    var totalPayoff: Int = 0

    /**
     * updates the game status after a turn has been played.
     */
    fun updateStatus(p: Player) {
        chain.add(p)
        activePopulation.remove(p)
        turn += 1
    }

    fun chainToString() :String {
        var str = "Chain of players that partake in the game:"

        if (chain.isEmpty()) {
            str += "∅"
        } else {
            for (p in chain) {
                str += "$p "
            }
        }

        return str
    }
}