package org.example.player

import org.example.game.Game
import kotlin.math.pow

/**
 * A Stochastic player in the SHPG, i.e. an irrational player which views decisions as a static stochastic process.
 *
 * @property [rejectAlterBelief] the belief that an alter will reject the potato at the next turn.
 * @property [id] unique identifier for the player.
 * @property [payoff] the payoff of the player.
 * @constructor creates a player without the hot potato, with a [payoff] of 0 and with [rejectAlterBelief] passed as argument.
 */
class StochasticPlayer(id: Int, val rejectAlterBelief: Double = 0.5) : Player(id) {
    init {
        require(rejectAlterBelief in 0.0..1.0)
    }

    /**
     * @param [currentTurn] the number of the turn being played.
     * @param [lifetime] the potato's lifetime
     * @param [remainingPlayers] the number of active players (i.e. that could take the hot potato, this player included)
     * @return the probability that the game will still continue(i.e. another player will take the potato).
     */
    private fun continueProbability(currentTurn: UInt, lifetime: UInt, remainingPlayers: Int) : Double {
        val remainingTurns = minOf(lifetime - currentTurn, (remainingPlayers - 1).toUInt())

        val result = (1 - rejectAlterBelief).pow(remainingTurns.toInt())
        return result
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

        val continueProbability = continueProbability(
            game.turn,
            potato.lifetime,
            game.activePopulation.size)
        val gainWeight = continueProbability * potato.gain.toDouble()

        val endProbability = 1 - continueProbability
        val lossWeight = endProbability * potato.loss.toInt()

        val decision = gainWeight >= lossWeight
        return decision


    }
}