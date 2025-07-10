package org.example.game

import org.example.player.Player
import org.example.potato.Potato

/**
 * Implements the Simple Hot Potato Game.
 *
 * @property [potato] the hot potato good associated to the game.
 * @property [activePlayers] the non-ordered subset of players which at the current turn never got the good.
 * @property [chain] the ordered set of players which at the current turn already got the good.
 * @property [turn] the current turn of the game.
 * @constructor creates a game with the given [potato] and set [activePlayers] (which at the beginning are all the
 * players of the game).
 */
class Game (
    private val potato: Potato,
    var activePlayers: MutableSet<Player>) {

    private var chain: MutableList<Player> = mutableListOf()   // Ordered sequence of players that did get the hot potato
    private var turn: Int = 0

    /**
     * Handles the game execution from start to end.
     */
    fun run() {
        var currentHolder = findingStartingPlayer()

        while(currentHolder != null) {
            updateGame(currentHolder)
            currentHolder = currentHolder.exchangePotato(this)
        }

        endGame()
    }

    /**
     * Gives the according payoff depending on if the player is the last one or not.
     * Recall that a player is the last one either if the good reached the end of its life or no other player was
     * willing to accept it.
     *
     * @param [isLastPlayer] flag passed by a player depending on the fact that it found another player to take the good
     * or not.
     *
     * @return the player's payoff.
     */
    fun returnPayoff (isLastPlayer: Boolean = false) : Int {
        return if (isLastPlayer) {
            potato.loss
        } else {
            potato.gain
        }
    }

    /**
     * @return true if the [potato] as not reached its end-of-life, false otherwise
     */
    fun isPotatoAlive () : Boolean{
        if (potato.lifetime < turn) {
            error("the current turn cannot exceed the potato's lifetime")
        }
        return potato.lifetime > turn
    }

    /**
     * Updates the game status.
     *
     * @param [player] the actor who decided to accept the good at the current [turn]
     */
    private fun updateGame (player: Player) {
        chain.add(player)
        activePlayers.remove(player)
        turn += 1
    }

    /**
     * Prints all information to the user regarding the ending of the game
     */
    private fun endGame () {
        TODO("The programmer has taken a nap. Hold on programmer!")
    }

    /**
     * Select the first player to start the game. It is chosen at random within the set.
     *
     * @return the chosen player if it was willing to take the good, otherwise null
     * (in this case the game will end immediately)
     */
    private fun findingStartingPlayer () : Player? {
        val randomPlayer = activePlayers.random()

        return if(randomPlayer.acceptPotato(this)) {
            randomPlayer
        } else {
            null
        }
    }
}