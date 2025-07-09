package org.example.game

import org.example.player.Player
import org.example.potato.Potato

/**
 * Simple Hot Potato Game implementation
 * players = chain U activePlayers
 */
class Game (val potato: Potato,
            val players: Set<Player>) {

    var chain: MutableList<Player> = mutableListOf()   // Ordered sequence of players that did get the hot potato
    var currentTurn: Int = 0
    var activePlayers: MutableSet<Player> = players.toMutableSet() // Keeps track of players that did not already get the hot potato

    /**
     * Handles the game execution
     */
    fun startGame() {
        // Select the starting player
        var currentHolder = findingStartingPlayer()

        // Running the game
        while(currentHolder != null) {
            updateGame(currentHolder)
            currentHolder = currentHolder.exchangePotato(this)
        }

        endGame()
    }

    /**
     * If the player is the last, return `loss`, otherwise `gain`.
     * This must be called when the player is actually able to give the potato to another player
     * (at least I think so)
     */
    fun returnPayoff (isLastPlayer: Boolean = false) : Int {
        return if (!isPotatoAlive() || isLastPlayer) {
            potato.loss
        } else if (currentTurn < potato.lifetime) {
            potato.gain
        } else {
            error("Error! current_turn > potato's lifetime")
        }
    }

    /**
     * Returns true if the current Turn has not reached the end-of-life of the potato, false otherwise
     */
    fun isPotatoAlive () : Boolean{
        if (potato.lifetime < currentTurn) {
            error("the current turn cannot exceed the potato's lifetime")
        }
        return potato.lifetime > currentTurn
    }

    /**
     * Updates the game status and sets.
     * Must call each time a new player decides to take the hot potato
     */
    private fun updateGame (player: Player) {
        chain.add(player)
        activePlayers.remove(player)
        currentTurn += 1
    }

    /**
     * Prints all information to the user regarding the ending of the game
     */
    private fun endGame () {
        TODO("The programmer has taken a nap. Hold on programmer!")
    }

    /**
     * Select the first player to start the game at random within the set.
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