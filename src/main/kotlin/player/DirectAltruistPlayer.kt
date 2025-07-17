package org.example.player

import org.example.game.Game
import kotlin.math.pow

/**
 * A Direct Altruist player in the SHPG, i.e. an irrational player which is concerned with the well-being of another
 * player, called beneficiary.
 * This means it will be willing to risk accepting the hot potato if its reject would cause a serious risk to
 * its beneficiary.
 * Still, is willing to help could also depend on how many alters among the active ones he/she think are willing to help
 * the same beneficiary. If that possibility is high, it might think taking the risk is not worth it.
 * For sake of simplicity we assume the beneficiary is a player always in the active set.
 *
 * @property [altruism] value between [0,1] representing how much the player is willing to help the beneficiary.
 * Default value is 1/2.
 * @property [helpAlterBelief] the belief of the current player that an alter will help the same beneficiary. Is a value
 * in [0,1]. Default value is 1/2.
 * @property [payoff] the payoff of the player.
 * @constructor creates a player without the hot potato, with a [payoff] of 0 and with [altruism] passed as argument.
 */
class DirectAltruistPlayer(id: Int, val altruism: Double = 0.5, val helpAlterBelief: Double = 0.5) : Player(id) {
    init {
        require(altruism in 0.0..1.0)
        require(helpAlterBelief in 0.0..1.0)
    }

    /**
     * Handles the decision logic of the player behind either the acceptance or denying of the hot potato.
     * The decision of a Direct Altruist player depends on how much it is interested in helping the beneficiary.
     *
     * @param [game] represents the current game state, which impacts the choice.
     * @return true if the player *chosen* to accept the good, false otherwise.
     */
    override fun decideAcceptance(game: Game): Boolean {
        val potato = game.potato
        val otherHelpers = minOf(potato.lifetime - game.turn, (game.activePopulation.size - 2).toUInt())
        val probAnotherAlterWillHelp = helpAlterBelief.pow(otherHelpers.toDouble())

        val gainWeight = potato.gain.toDouble() * (1 + (0.5 * altruism))
        val lossWeight = if (probAnotherAlterWillHelp != 1.0) {
            potato.loss.toDouble() / (altruism * (1 - probAnotherAlterWillHelp))
        } else {
            potato.loss.toDouble()
        }

        val decision = gainWeight > lossWeight
        return decision
    }
}