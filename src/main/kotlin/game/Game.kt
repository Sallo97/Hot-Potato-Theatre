package org.example.game

import org.example.player.Player
import org.example.potato.Potato

/**
 * Implements the Simple Hot Potato Game.
 *
 * @property [potato] the hot potato good associated to the game.
 * @property [activePopulation] the non-ordered subset of players which at the current turn never got the good.
 * @property [chain] the ordered set of players which at the current turn already got the good.
 * @property [turn] the current turn of the game.
 * @property [numOfPlayers] the total number of players in the game.
 * @constructor creates a game with the given [potato] and population.
 */
class Game (
    val potato: Potato,
    var activePopulation: MutableSet<Player>) {
    private var chain: MutableList<Player> = mutableListOf()
    var turn: Int = 0
    val numOfPlayers = activePopulation.size

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
     *
     * @param [isLastPlayer] flag passed by a player to specify if it is the last player or not.
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
     * @return true if the [potato] has not reached its end-of-life, false otherwise
     */
    fun isPotatoAlive () : Boolean{
        if (potato.lifetime < turn) {
            error("ERROR! Potato's lifetime (${potato.lifetime}) < current turn ($turn).\n The current turn  cannot " +
                    "exceed the potato's lifetime ")
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
        activePopulation.remove(player)
        turn += 1
    }

    /**
     * Prints the state of the game (total payoff, final chain, etc...) after it finishes.
     */
    private fun endGame () {
        var totalPayoff = 0

        println("Game ended with the following:")

        println("- potato's lifetime = ${potato.lifetime}\t turns = $turn")


        print("- Chain of players that partake in the game:\t")
        for (p in chain) {
            print(p.getInformation() + "\t")
            totalPayoff += p.payoff
        }
        println()

        print("- Total payoff = $totalPayoff")
    }

    /**
     * Select the first player to start the game. It is chosen at random within the set.
     *
     * @return the chosen player if it was willing to take the good, otherwise null
     * (in this case the game will end immediately)
     */
    private fun findingStartingPlayer () : Player? {
        val randomPlayer = activePopulation.random()

        return if(randomPlayer.acceptPotato(this)) {
            randomPlayer
        } else {
            null
        }
    }
}