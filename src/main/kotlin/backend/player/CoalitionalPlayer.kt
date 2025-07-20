package backend.player

import backend.coalition.Coalition
import backend.game.Game
import backend.potato.Potato
import kotlin.math.absoluteValue

/**
 * A Coalitional player in the SHPG, i.e. an irrational player concerned in forming a coalition with other players of the
 * same type in order to get a better payoff.
 *
 * @property [acceptanceToRisk] how much the current players is willing to risk accepting the potato for the proposed
 * coalition. Is a value in [0,1]. Default value is 1/2.
 * @property [payoff] the payoff of the player.
 * @constructor creates a player without the hot potato, with a [payoff] of 0 and with [acceptanceToRisk] passed as argument.
 */
class CoalitionalPlayer(
    id:Int,
    val acceptanceToRisk: Double) : Player(id, PlayerType.COALITIONAL) {
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

        // If at least one player think is worth being in a coalition, then all others will accept immediately.
        if (coalition.size() > 0) {
            return true
        }

        val worstCasePayoff = computeWorstCasePayoff(coalition, potato)
        val bestCasePayoff = computeBestCasePayoff(coalition, potato)

        val playerLeft = coalition.possibleMembers - 1 // removing the current player
        val playerLeftWeight : Double = playerLeft * 0.8
        val bestCaseWeight: Double = bestCasePayoff * (playerLeftWeight * acceptanceToRisk)

        val decision = if(worstCasePayoff > 0) {
            true
        } else {
            if(bestCasePayoff < 0) {
                false
            }
             bestCaseWeight > worstCasePayoff.absoluteValue
        }
        return decision
    }

    override fun toString(): String {
        val str = "${super.toString()} acceptanceToRisk: $acceptanceToRisk}"
        return str
    }
}

/**
 * @return the worst case payoff for the potential coalition, that is if it terminates after the current player
 * accepts the potato.
 */
private fun computeWorstCasePayoff(coalition: Coalition, potato: Potato): Double {
    val worstCaseTotalPayoff: Double = (coalition.totalPayoff - potato.baseLoss) // totalPayoff of the coalition when the current player becomes the last one.
    val worstCasePayoff = worstCaseTotalPayoff / (coalition.size() + 1)

    return worstCasePayoff
}

/**
 * @return the best case payoff for the potential coalition, that is if it terminates after all coalitional players
 * accepted to be in the coalition.
 */
private fun computeBestCasePayoff(coalition: Coalition, potato: Potato) : Double{
    val allPlayersExceptLast = coalition.totalCoalitionalPlayers - 1 // the Last will not get the gain
    val bestCaseTotalPayoff: Double = (allPlayersExceptLast * potato.baseGain) - potato.baseLoss // totalPayoff if all coalitional players enter the coalition.
    val bestCasePayoff: Double = bestCaseTotalPayoff / coalition.totalCoalitionalPlayers

    return bestCasePayoff
}