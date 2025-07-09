package org.example.player

import org.example.game.Game

/**
 * An abstract representation of a player in the SHPG.
 * A player is defined by its payoff and if it holds the hot potato or not.
 * Each player needs th have methods to decide if he/she accept the potato or not
 * and for proposing to other players to get the hot potato.
 */
abstract class Player {
    // PROPERTIES
    var owns_potato: Boolean = false
    var payoff: Int = 0

    // METHODS
    /**
     * Implements the behavior of the player in deciding either to take or not the hot potato good.
     * This method is abstract since it strictly depends on the player.
     */
    abstract fun decisionMaking (game: Game): Boolean

    /**
     * It is called by other players if they want to propose the current player to take the hot potato or not.
     * It returns a boolean stating if the player accepted or not.
     */
    fun acceptPotato (game: Game): Boolean {
        owns_potato = decisionMaking(game)
        if (owns_potato) {
            payoff += game.returnPayoff()  // TODO Not sure if the payoff should be granted before
                                            // actually seeing if the player is able to exchange it.
                                            // Also what if a player gets it and then is not able to exchange it?
                                            // It gets both gain and loss?
        }
        return owns_potato
    }

    /**
     * Tries to find another player willing to take the hot potato.
     */
    fun exchangePotato (game:Game) : Player? {
        if (!owns_potato) {
            error("A player cannot try to exchange a potato only if he/she is actually holding one!")
        }

        // Trying to find a sucker...
        if (game.isPotatoAlive()) {
            for (p in game.activePlayers) {
                if (p.acceptPotato(game)){
                    owns_potato = false
                    return p
                }
            }
        }

        // Sucker not found, the player has become the last one
        payoff += game.returnPayoff(isLastPlayer = true)
        return null
    }



}