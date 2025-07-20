package org.example.backend.player

import org.example.backend.game.Game
import kotlin.math.pow

/**
 * A Stochastic player in the SHPG, i.e. an irrational player which views decisions as a static stochastic process.
 *
 * @property [alterDenyBelief] the belief that an alter at the next turn will deny the hot potato good. Is a value between [0,1]
 * @property [id] unique identifier for the player.
 * @property [payoff] the payoff of the player.
 * @constructor creates a player without the hot potato, with a [payoff] of 0 and with [alterDenyBelief] passed as argument.
 */
class StochasticPlayer(id: Int, val alterDenyBelief: Double = 0.5) : Player(id) {
    init {
        require(alterDenyBelief in 0.0..1.0)
    }


    /**
     * Handles the decision logic of the player behind either the acceptance or denying of the hot potato.
     * The decision of a Stochastic player depends on the probability that the game could end at the current turn.
     *
     * @param [game] represents the current game state, which impacts the choice.
     * @return true if the player *chosen* to accept the good, false otherwise.
     */
    override fun decideAcceptance(game: Game): Boolean {
        val potato = game.potato
        val remainingPlayers = game.getNumOfAvailablePlayers() - 1

        val probGameEndsNextTurn = alterDenyBelief.pow(remainingPlayers)
        val probGameContinuesNextTurn = 1 - probGameEndsNextTurn

        val gainWeight = probGameContinuesNextTurn * potato.gain
        val lossWeight = probGameEndsNextTurn * potato.loss

        val decision = gainWeight > lossWeight
        return decision
    }
}