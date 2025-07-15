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
 * @property [totalPayoff] sum of the payoff of all players at the current turn.
 * @constructor creates a game with the given [potato] and population.
 */
class Game (
    val potato: Potato,
    var activePopulation: MutableSet<Player>) {
    private var chain: MutableList<Player> = mutableListOf()
    var turn: UInt = 0u
    val numOfPlayers: UInt = activePopulation.size.toUInt()
    var totalPayoff = 0

    /**
     * Handles the game execution from start to end.
     */
    fun run() {
        var foundNewHolder = findingStartingPlayer()

        while(foundNewHolder) {
            updateGame()
            foundNewHolder = potato.currentHolder!!.exchangePotato(this)
        }

        endGame()
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
    private fun updateGame () {
        potato.currentHolder!!.let {
            chain.add(it)
            activePopulation.remove(it)
        }
        turn += 1u
    }

    /**
     * @return the payoff associated to the player and updates totalPayoff accordingly.
     * @param [isLastPlayer] flag passed by a player to specify if it is the last player or not.
     *
     */
    fun getPayoff(isLastPlayer: Boolean = false) : Int {
        val payoff = potato.getPayoff(isLastPlayer)
        totalPayoff += payoff

        return payoff
    }

    /**
     * Prints the state of the game (total payoff, final chain, etc...) after it finishes.
     */
    private fun endGame () {

        println("Game ended with the following:")
        println("- Potato's lifetime = ${potato.lifetime}\t turns = $turn")
        println("- ${chainToString()}")
        println("- Total payoff = $totalPayoff")
    }

    private fun chainToString() :String {
        var str = "Chain of players that partake in the game:"

        if (chain.isEmpty()) {
            str += "∅"
        } else {
            for (p in chain) {
                str += "$p "
            }
        }

        return str
    }

    /**
     * Select the first player to start the game. It is chosen at random within the set.
     *
     * @return the chosen player if it was willing to take the good, otherwise null
     * (in this case the game will end immediately)
     */
    private fun findingStartingPlayer () : Boolean {
        val randomPlayer = activePopulation.random()

        if(randomPlayer.decideAcceptance(this)) {
            potato.currentHolder = randomPlayer
            return true
        } else {
            return false
        }
    }

    override fun toString(): String {
        return "{ turn: $turn; numOfPlayers: $numOfPlayers; numOfRemainingPlayers: ${activePopulation.size} }"
    }
}