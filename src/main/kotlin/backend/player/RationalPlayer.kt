package org.example.backend.player

import org.example.backend.game.Game

/**
 * Represents a rational player in the SHPG.
 *
 * @property [id] unique identifier for the player.
 * @property [payoff] the payoff of the player. At the start it is always 0.
 * @constructor creates a player without the hot potato and with a payoff of 0.
 */
class RationalPlayer(id: Int) : Player(id) {

    /**
     * Handles the decision logic of the player behind either the acceptance or denying of the hot potato.
     *
     * @return false, a rational player will never accept a hot potato good.
     */
    override fun decideAcceptance (game: Game) : Boolean {
        return false
    }
}