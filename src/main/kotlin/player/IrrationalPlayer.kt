package org.example.player

import org.example.game.Game

/**
 * Represents an irrational player in the SHPG, i.e. one which will always take the good if asked.
 *
 * @property [id] unique identifier for the player.
 * @property [owns_potato] describes as a boolean if the player is the current holder of the hot potato good or not.
 * At the start a player hasn't the good.
 * @property [payoff] the payoff of the player. At the start it is always 0.
 * @constructor creates a player without the hot potato and with a payoff of 0.
 */
class IrrationalPlayer (id: Int) : Player(id) {

    /**
     * Handles the decision logic of the irrational player in taking or not the good.
     *
     * @return true, since a rational player will always accept a hot potato good.
     */
    override fun decisionMaking(game: Game): Boolean {
        return true
    }
}