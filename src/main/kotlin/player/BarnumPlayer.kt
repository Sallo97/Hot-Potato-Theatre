package org.example.player

import org.example.game.Game
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
class BarnumPlayer (id: Int, val prob: Double) : Player(id) {
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
        if(game.isPotatoAlive() && game.activePopulation.isNotEmpty()) {
            val probPow = prob.pow(game.activePopulation.size - 1)
            val probRatio = probPow /  (1 - probPow)

            return game.potato.ratio >= probRatio
        }
        return false
    }

    override fun toString(): String {
        return "{id: $id; payoff: $payoff; prob: $prob}"
    }
}