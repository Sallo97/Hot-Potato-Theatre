package org.example.potato

import org.example.player.Player

/**
 * Defines a hot potato object.
 *
 * @property [lifetime] the number of turn in which the hot potato is still valid (i.e. the payoff is a gain).
 * @property [gain] the payoff for all players that take the hot potato and are not the last player. It is always positive.
 * @property [loss] the payoff for the last player that takes the hot potato. It is always negative.
 * @property [ratio] the positive ratio between gain and loss (i.e. gain / (-loss)).
 * @property [currentHolder] the current player holding the potato. At the start no one holds it.
 * We interpret gain and loss value as dollars for sake of simplicity
 */
data class Potato(val lifetime:UInt, val gain:UInt, val loss:UInt) {
    val ratio : Double = gain.toDouble()/(loss.toDouble())
    var currentHolder: Player? = null

    override fun toString(): String {
        return "{ lifetime: $lifetime; gain: $gain; loss: $loss; ratio: $ratio }"
    }

    /**
     * Gives the according payoff depending on if the player is the last one or not.
     *
     * @param [isLastPlayer] flag passed by a player to specify if it is the last player or not.
     * @return the player's payoff.
     */
    fun getPayoff (isLastPlayer: Boolean = false) : Int {
        return if (isLastPlayer) {
            -loss.toInt()
        } else {
            gain.toInt()
        }
    }

    /**
     * @return true if [p] is the current holder, false otherwise.
     */
    fun isOwner(p: Player) : Boolean {
        return p == currentHolder
    }
}