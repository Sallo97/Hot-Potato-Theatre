package org.example.backend.player

import org.example.backend.game.Game

/**
 * A Coalitional player in the SHPG, i.e. an irrational player concerned in forming a coalition with other players of the
 * same type in order to get a better payoff.
 *
 * @property [acceptanceToRisk] how much the current players is willing to risk accepting the potato for the proposed
 * coalition. Is a value between 0 and 1. Default value is 1/2.
 * @property [payoff] the payoff of the player.
 * @constructor creates a player without the hot potato, with a [payoff] of 0 and with [acceptanceToRisk] passed as argument.
 */
class CoalitionalPlayer(id:Int, val acceptanceToRisk: Double) : Player(id) {
    init {
        require(acceptanceToRisk in 0.0..1.0)
    }

    /**
     * Handles the decision logic of the player behind either the acceptance or denying of the hot potato.
     * The decision of a Coalition player depends on how much the player gains by entering the available coalition.
     * For sake of simplicity we assume only a coalition can be formed.
     *
     * @param [game] represents the current game state, which impacts the choice.
     * @return true if the player *chosen* to accept the good, false otherwise.
     */
    override fun decideAcceptance(game: Game): Boolean {
        val potato = game.potato
        val coalition = game.coalition

        val worstCaseTotalPayoff: Double = (coalition.totalPayoff - potato.loss.toDouble()) // totalPayoff of the coalition when the current player becomes the last one.
        val worstCasePayoff: Double = worstCaseTotalPayoff / coalition.size()

        val bestCaseTotalPayoff: Double = (coalition.totalCoalitionalPlayers.toDouble() * potato.gain.toDouble()) - potato.loss.toDouble() // totalPayoff if all coalitional players enter the coalition.
        val bestCasePayoff: Double = bestCaseTotalPayoff / coalition.totalCoalitionalPlayers.toDouble()

        val decision = if(worstCasePayoff > 0) {
            true
        } else {
            bestCasePayoff * acceptanceToRisk > potato.loss.toDouble()
        }
        return decision
    }

    override fun toString(): String {
        return "{id: $id; payoff: $payoff; acceptanceToRisk: $acceptanceToRisk}"
    }
}