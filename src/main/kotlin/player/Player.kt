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
    var payoff: Int = 0

    /**
     * Handles the decision logic of the player behind either the acceptance or denying of the hot potato.
     *
     * @param [game] represents the current game state, which could impact the choice.
     * @return true if the player *chosen* to accept the good, false otherwise.
     */
    abstract fun decideAcceptance (game: Game): Boolean

    /**
     * Search among the active population (i.e. those that did not already take the hot potato), the first one willing to
     * take the good.
     *
     * @param [game] represents the current state of the game.
     * @return true if a new player to exchange the good was found, false otherwise
     */
    fun exchangePotato (game:Game) : Boolean {
        val potato = game.potato
        if (!potato.isOwner(this)) {
            error("players is not holding the good right now.\n" +
                    "A player can exchange the hot potato only if he/she is holding it during the current turn!")
        }

        // Trying to find a good samaritan to take the good...
        if (game.isPotatoAlive()) {
            for (p in game.activePopulation) {
                if (p.decideAcceptance(game)){
                    potato.currentHolder = p
                    break
                }
            }
        }

        val isOwner = potato.isOwner(this)
        payoff += game.getPayoff(isLastPlayer = isOwner)
        return !isOwner
}

    override fun toString() : String {
        return "{id: $id; payoff: $payoff}"
    }
}