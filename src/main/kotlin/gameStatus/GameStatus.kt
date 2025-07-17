package org.example.gameStatus

import org.example.player.Player

/**
 * Contains useful information regarding the status of a SHPG
 *
 *
 */
data class GameStatus(
    val activePopulation: MutableSet<Player>,
) {
    private val chain: MutableList<Player> = mutableListOf()
    var turn: Int = 0
    val numOfPlayers: Int = activePopulation.size
    var totalPayoff = 0

    /**
     * updates the game status after a turn has been played.
     */
    fun updateStatus(p: Player) {
        chain.add(p)
        activePopulation.remove(p)
        turn += 1
    }
}