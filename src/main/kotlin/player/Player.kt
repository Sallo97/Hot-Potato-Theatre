package org.example.player

import org.example.game.Game

/**
 * Represents the common abstract behavior of all types of players in the SHPG.
 *
 * @property [id] unique identifier for the player.
 * @property [ownsPotato] describes as a boolean if the player is the current holder of the hot potato good or not.
 * @property [payoff] the payoff of the player.
 * @constructor creates a player without the hot potato and with a payoff of 0.
 */
abstract class Player (val id: Int){
    var ownsPotato: Boolean = false
    var payoff: Int = 0

    /**
     * Handles the decision logic of the player behind either the acceptance or denying of the hot potato.
     *
     * @param [game] represents the current game state, which could impact the choice.
     * @return true if the player *chosen* to accept the good, false otherwise.
     */
    abstract fun decisionMaking (game: Game): Boolean

    /**
     * Handles the bargaining process of accepting or not a hot potato proposed by another player.
     *
     * @param [game] represents the current state of the game.
     * @return true if the player *got* the hot potato, false otherwise.
     */
    fun acceptPotato (game: Game): Boolean {
        ownsPotato = decisionMaking(game)
        return ownsPotato
    }

    /**
     * Search among the active population (i.e. those that did not already take the hot potato), the first one willing to
     * take the good.
     *
     * @param [game] represents the current state of the game.
     * @return the player that accepted the hot potato or null if no one was willing.
     */
    fun exchangePotato (game:Game) : Player? {
        if (!ownsPotato) {
            error("players is not holding the good right now.\n" +
                    "A player can exchange the hot potato only if he/she is holding it during the current turn!")
        }

        // Trying to find a good samaritan to take the good...
        var sucker: Player? = null
        if (game.isPotatoAlive()) {
            for (p in game.activePopulation) {
                if (p.acceptPotato(game)){
                    sucker = p
                    ownsPotato = false
                    break
                }
            }
        }

        payoff += game.returnPayoff(isLastPlayer = ownsPotato)
        return sucker
    }

    override fun toString() : String {
        return "{id: $id; ownsPotato: $ownsPotato; payoff: $payoff}"
    }
}