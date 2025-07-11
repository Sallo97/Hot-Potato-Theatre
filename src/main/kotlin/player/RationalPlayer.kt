package org.example.player

import org.example.game.Game

/**
 * Represents a rational player in the SHPG.
 *
 * @property [id] unique identifier for the player.
 * @property [ownsPotato] describes as a boolean if the player is the current holder of the hot potato good or not.
 * At the start a player hasn't the good.
 * @property [payoff] the payoff of the player. At the start it is always 0.
 * @constructor creates a player without the hot potato and with a payoff of 0.
 */
class RationalPlayer(id: Int) : Player(id) {

    /**
     * Handles the decision logic of the player behind either the acceptance or denying of the hot potato.
     *
     * @return false, a rational player will never accept a hot potato good.
     */
    override fun decisionMaking (game: Game) : Boolean {
        return false
    }
}