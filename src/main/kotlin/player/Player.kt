package org.example.player

import org.example.game.Game

/**
 * Represents the common abstract behavior of all types of players in the SHPG.
 *
 * @property [owns_potato] describes as a boolean if the player is the current holder of the hot potato good or not.
 * At the start a player hasn't the good.
 * @property [payoff] the payoff of the player. At the start it is always 0.
 * @constructor creates a player without the hot potato and with a payoff of 0.
 */
abstract class Player {
    // PROPERTIES
    var owns_potato: Boolean = false
    var payoff: Int = 0

    // METHODS
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
        owns_potato = decisionMaking(game)
        return owns_potato
    }

    /**
     * Search among the players who have not already got the hot potato, the first one willing to take it among them.
     *
     * @param [game] represents the current state of the game.
     * @return the player that accepted the hot potato or null if no one was willing.
     */
    fun exchangePotato (game:Game) : Player? {
        if (!owns_potato) {
            error("A player can exchange the hot potato only if he/she is actually holding it!")
        }

        // Trying to find a good samaritan to take the good...
        if (game.isPotatoAlive()) {
            for (p in game.activePlayers) {
                if (p.acceptPotato(game)){
                    owns_potato = false
                    payoff += game.returnPayoff()   // The player is not the last one, since it found a sucker willing to take it, thus it gets a gain to its payoff.
                    return p
                }
            }
        }

        payoff += game.returnPayoff(isLastPlayer = true)    // The player was not able to find another sucker, this it is the last player and gets a loss to its payoff.
        return null
    }



}