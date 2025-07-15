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
     * @return the final state of the game (total payoff, final chain, etc...).
     */
    fun getEndGameInfo () : String {
        val str = "Game ended with the following:\n" +
        "- Potato's lifetime = ${potato.lifetime}\t turns = $turn\n" +
        "- ${chainToString()}\n"
        "- Total payoff = $totalPayoff\n"
        return str
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
     * @param [tryAll] if true check over all the set for a willing player, otherwise will check only a single random
     * player.
     * @return true if a player was willing to accept the good, false otherwise.
     */
    private fun findingStartingPlayer (tryAll: Boolean = false) : Boolean {
        var found: Player? = null

        if (tryAll) {
            for (p in activePopulation) {
                if(p.decideAcceptance(this)) {
                    found = p
                    break
                }
            }
        } else {
            val randomPlayer = activePopulation.random()

            if(randomPlayer.decideAcceptance(this)) {
                found = randomPlayer
            }
        }

        potato.currentHolder = found
        return found != null
    }

    /**
     * @return the chain size
     */
    fun getChainSize() : Int {
        return chain.size
    }

    override fun toString(): String {
        return "{ turn: $turn; numOfPlayers: $numOfPlayers; numOfRemainingPlayers: ${activePopulation.size} }"
    }
}