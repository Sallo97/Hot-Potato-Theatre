package org.example.player

import org.example.game.Game

/**
 * Represents the common abstract behavior of all types of players in the SHPG.
 *
 * @property [id] unique identifier for the player.
 * @property [payoff] the payoff of the player.
 * @constructor creates a player without the hot potato and with an initial payoff of 0.
 */
abstract class Player (val id: Int){
    var payoff: Double = 0.0

    /**
     * Handles the decision logic of the player behind either the acceptance or denying of the hot potato.
     *
     * @param [game] represents the current game state, which could impact the choice.
     * @return true if the player *chosen* to accept the good, false otherwise.
     */
    abstract fun decideAcceptance (game: Game): Boolean

    override fun toString() : String {
        return "{id: $id; payoff: $payoff}"
    }
}