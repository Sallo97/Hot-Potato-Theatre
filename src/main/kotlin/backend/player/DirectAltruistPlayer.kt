package backend.player

import backend.game.Game
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
 * @property [alterBelief] the belief of the current player that an alter will help the same beneficiary. Is a value > 0.
 * Default value is 1
 * @property [payoff] the payoff of the player.
 * @constructor creates a player without the hot potato, with a [payoff] of 0 and with [altruism] passed as argument.
 */
class DirectAltruistPlayer(
    id: Int, val altruism: Double = 0.5,
    val alterBelief: Double = 1.0) : Player(id, PlayerType.DIRECT_ALTRUIST) {
    init {
        require(altruism in 0.0..1.0)
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

        val otherHelpers = minOf(0, game.getNumOfAvailablePlayers() - 2) // remove both current player and beneficiary.
        val responsibilityScaling: Double = if (otherHelpers == 0) {
            0.0
        } else {
            1 / otherHelpers.toDouble().pow(alterBelief)
        }

        val gainWeight: Double = potato.currentGain * responsibilityScaling
        val lossWeight: Double = potato.currentLoss * (1 - altruism)

        val decision = gainWeight > lossWeight
        return decision
    }

    override fun toString(): String {
        val str = "${super.toString()} altruism: $altruism; helpAlterBelief: $alterBelief }"
        return str
    }
}