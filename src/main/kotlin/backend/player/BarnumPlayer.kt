package backend.player

import backend.game.Game
import kotlin.math.pow

/**
 * A Barnum player in the SHPG, i.e. a rational player aware that there is the *possibility* that irrational players
 * are among the population.
 *
 * @property [prob] the probability that any other player in the population is a Barnum.
 * It is represented as a value between 0 and 1 (included).
 * @property [id] unique identifier for the player.
 * @property [payoff] the payoff of the player.
 * @constructor creates a player without the hot potato, with a [payoff] of 0 and with [prob] passed as argument.
 */
class BarnumPlayer (id: Int, val prob: Double) : Player(id, PlayerType.BARNUM) {
    init {
        require(prob in 0.0..1.0)
    }
    /**
     * Handles the decision logic of the player behind either the acceptance or denying of the hot potato.
     * The decision of a Barnum player depends on the state of the game (the number of remaining players and
     * if the hot potato is alive or not) and on the relationship between the ratio of gain and loss and the
     * ratio between the probability that alters are Barnum or irrational.
     *
     * @param [game] represents the current game state, which impacts the choice.
     * @return true if the player *chosen* to accept the good, false otherwise.
     */
    override fun decideAcceptance(game: Game): Boolean {
        val potato = game.potato
        val remainingTurns = game.getRemainingTurnsExceptCurrent()

        if(!game.isPotatoAlive() || game.getRemainingTurnsWithCurrent() <= 0) {
            return false
        }

        val probPow = prob.pow(remainingTurns)
        val probRatio = probPow /  (1 - probPow)

        return potatoAcceptance(potato.ratio, probRatio, potato)
    }

    override fun toString(): String {
        val str = "${super.toString()} prob: $prob}"
        return str
    }
}