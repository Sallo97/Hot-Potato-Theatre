package org.example.player

import org.example.game.Game
import kotlin.math.pow

/**
 * a Barnum player in the SHPG, i.e. a rational player aware that there is the *possibility* that irrational players
 * are among the population.
 *
 * @property [prob] the probability that any other player in the population is a Barnum.
 * @property [id] unique identifier for the player.
 * @property [owns_potato] describes as a boolean if the player is the current holder of the hot potato good or not.
 * At the start a player hasn't the good.
 * @property [payoff] the payoff of the player. At the start it is always 0.
 * @constructor creates a player without the hot potato and with a payoff of 0.
 */
class BarnumPlayer (id: Int, prob: Float) : Player(id) {
    val prob = if (prob <= 1.0 && prob >= 0) {
        prob
    } else {
        error("prob should be a value between 0 and 1!")
    }

    /**
     * Handles the decision logic of the player behind either the acceptance or denying of the hot potato.
     * The decision of a Barnum player depends on the state of the game (the number of remaining players and
     * if the hot portato is alive or not) and on the relationship between the ratio of gain and loss and the ratio between the probability that alters are Barnums or irrational.
     *
     * @param [game] represents the current game state, which impacts the choice.
     * @return true if the player *chosen* to accept the good, false otherwise.
     */
    override fun decisionMaking(game: Game): Boolean {
        if(game.isPotatoAlive() && game.activePopulation.isNotEmpty()) {
            val probPow = prob.pow(game.numOfPlayers - game.turn - 1)
            val probRatio = probPow /  (1 - probPow)

            return game.potato.ratio >= probRatio
        }
        return false
    }
}