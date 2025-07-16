package org.example.player

import org.example.game.Game
import kotlin.math.pow

/**
 * A Barnum player in the SHPG, i.e. an irrational player which views decisions as a static stochastic process.
 *
 * @property [weight] fixed hyperparameter to compute the probability.
 * @property [id] unique identifier for the player.
 * @property [payoff] the payoff of the player.
 * @constructor creates a player without the hot potato, with a [payoff] of 0 and with [weight] passed as argument.
 */
class StochasticPlayer(id: Int, val weight: Double) : Player(id) {

    /**
     * Determines the current probability that the chain ends at the current [turn]
     *
     * @return the computed probability value which must be between 0 and 1.
     */
    private fun terminationProb(turn: Int) : Double {
        val pow = (1 - weight).pow(turn - 1)
        return pow * weight
    }

    /**
     * Handles the decision logic of the player behind either the acceptance or denying of the hot potato.
     * The decision of a Stochastic player depends on the probability that the game could end at the current turn.
     *
     * @param [game] represents the current game state, which impacts the choice.
     * @return true if the player *chosen* to accept the good, false otherwise.
     */
    override fun decideAcceptance(game: Game): Boolean {
        val terminationProb = terminationProb(game.turn.toInt())
        val gainWeight = (1 - terminationProb) * game.potato.gain.toDouble()
        val lossWeight = terminationProb * game.potato.loss.toDouble()
        val accept = gainWeight >= lossWeight
        return accept
    }
}