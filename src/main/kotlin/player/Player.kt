package org.example.player

import org.example.game.Game

/**
 * Represents the common abstract behavior of all types of players in the SHPG.
 *
 * @property [id] unique identifier for the player.
 * @property [owns_potato] describes as a boolean if the player is the current holder of the hot potato good or not.
 * At the start a player hasn't the good.
 * @property [payoff] the payoff of the player. At the start it is always 0.
 * @constructor creates a player without the hot potato and with a payoff of 0.
 */
abstract class Player (val id: Int){
    var owns_potato: Boolean = false
    var payoff: Int = 0

    /**
     * @return a string containing the player's information
     */
    fun getInformation() : String {
        return "{ [id] = $id ; [payoff] = $payoff }"
    }

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
            error("players is not holding the good right now." +
                    "A player can exchange the hot potato only if he/she is holding it during the current turn!")
        }

        // Trying to find a good samaritan to take the good...
        var sucker: Player? = null
        if (game.isPotatoAlive()) {
            for (p in game.activePlayers) {
                if (p.acceptPotato(game)){
                    sucker = p
                }
            }
        }

        if (sucker != null) {
            // The player is not the last one.
            owns_potato = false
            payoff += game.returnPayoff()
        } else {
            // The player is the last one.
            payoff += game.returnPayoff(isLastPlayer = true)    // The player was not able to find another sucker, this it is the last player and gets a loss to its payoff.
        }

        return sucker
    }



}