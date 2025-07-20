package backend.player

import backend.game.Game

/**
 * Represents the common abstract behavior of all types of players in the SHPG.
 *
 * @property [id] unique identifier for the player.
 * @property [payoff] the payoff of the player.
 * @property [behavior] describes the player behavior.
 * @constructor creates a player without the hot potato and with an initial payoff of 0.
 */
sealed class Player (val id: Int, val behavior: PlayerType){
    var payoff: Double = 0.0

    /**
     * Handles the decision logic of the player behind either the acceptance or denying of the hot potato.
     *
     * @param [game] represents the current game state, which could impact the choice.
     * @return true if the player *chosen* to accept the good, false otherwise.
     */
    abstract fun decideAcceptance (game: Game): Boolean

    override fun toString() : String {
        return "$behavior {id: $id; payoff: $payoff "
    }
}