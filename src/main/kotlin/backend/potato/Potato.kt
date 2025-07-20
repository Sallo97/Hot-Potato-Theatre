package backend.potato

import backend.player.Player

/**
 * Defines a hot potato object.
 *
 * @property [lifetime] the number of turn in which the hot potato is still valid (i.e. the payoff is a gain).
 * @property [gain] the payoff for all players that take the hot potato and are not the last player. It is always positive.
 * @property [loss] the payoff for the last player that takes the hot potato. It is always negative.
 * @property [gainFactor] by how much the gain changes over a turn.
 * @property [loss] by how much the loss changes over a turn.
 * @property [ratio] the positive ratio between gain and loss (i.e. gain / (-loss)).
 * @property [currentHolder] the current player holding the potato. At the start no one holds it.
 * We interpret gain and loss value as dollars for sake of simplicity
 */
data class Potato(val lifetime:Int, var gain:Double, var loss:Double, val gainFactor: Double = 1.0, val lossFactor: Double = 1.0) {
    val ratio : Double = gain/(loss)
    var currentHolder: Player? = null

    init {
        require(gain != 0.0)
        require(loss != 0.0)
    }

    override fun toString(): String {
        return "{ lifetime: $lifetime; gain: $gain; gainFactor: $gainFactor; loss: $loss; lossFactor: $lossFactor }"
    }

    /**
     * Gives the according payoff depending on if the player is the last one or not.
     *
     * @param [isLastPlayer] flag passed by a player to specify if it is the last player or not.
     * @return the player's payoff.
     */
    fun getPayoff (isLastPlayer: Boolean = false) : Double {
        return if (isLastPlayer) {
            -loss
        } else {
            gain
        }
    }

    /**
     * Updates the potato gain and loss after a turn by their factors.
     */
    fun updatePotato () {
        gain *= gainFactor
        loss *= lossFactor
    }
}