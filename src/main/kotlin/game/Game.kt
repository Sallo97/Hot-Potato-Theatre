package org.example.game

import org.example.coalition.Coalition
import org.example.player.CoalitionalPlayer
import org.example.player.Player
import org.example.potato.Potato

/**
 * Implements the Simple Hot Potato Game.
 *
 * @property [potato] the hot potato good associated to the game.
 * @property [coalition] the active coalition of coalitional players in the game.
 * @property [activePopulation] the non-ordered subset of players which at the current turn never got the good.
 * @property [chain] the ordered set of players which at the current turn already got the good.
 * @property [turn] the current turn of the game.
 * @property [numOfPlayers] the total number of players in the game.
 * @property [totalPayoff] sum of the payoff of all players at the current turn.
 * @constructor creates a game with the given [potato] and population.
 */
class Game (
    val potato: Potato,
    val activePopulation: MutableSet<Player>) {
    val coalition: Coalition
    private val chain: MutableList<Player> = mutableListOf()
    var turn: UInt = 0u
    val numOfPlayers: UInt = activePopulation.size.toUInt()
    var totalPayoff = 0

    init {
        val coalitionSetSize = activePopulation.filter { it is CoalitionalPlayer }.size.toUInt()
        coalition = Coalition(coalitionSetSize)
    }

    /**
     * Handles the game execution from start to end.
     * @param [tryAll] if true for finding the starting player check over all the set for a willing player, otherwise
     * will check only a single random player.
     */
    fun run(tryAll: Boolean = false) {
        var foundNewHolder = findingStartingPlayer(tryAll)

        while(foundNewHolder) {
            val holder = potato.currentHolder!!
            updateGame(holder)

            foundNewHolder = holder.exchangePotato(this)
            givePayoff(holder, foundNewHolder)
        }

        // Game Ended, let the coalition split their total payoff among the members
        coalition.splitTotalPayoff()
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

    /**
     * @return the chain size
     */
    fun getChainSize() : Int {
        return chain.size
    }

    override fun toString(): String {
        return "{ turn: $turn; numOfPlayers: $numOfPlayers; numOfRemainingPlayers: ${activePopulation.size} }"
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
     * @param [p] the actor who decided to accept the good at the current [turn]
     */
    private fun updateGame (p: Player) {
        p.let {
            chain.add(it)
            activePopulation.remove(it)
            if (it is CoalitionalPlayer) {
                coalition.addMember(it)
            }
        }
        turn += 1u
    }

    /**
     * Gives the payoff to player [p] and updates game status. Be aware that if [p] is a coalitional player, then it
     * gives the payoff to the coalition instead.
     * @param [isLastPlayer] specifies of if [p] is the last player or not.
     */
     private fun givePayoff(p:Player, isLastPlayer: Boolean = false) {
        val payoff = potato.getPayoff(isLastPlayer)
        totalPayoff += payoff

        if (p !is CoalitionalPlayer) {
            p.payoff += payoff
        } else {
            coalition.totalPayoff += payoff
        }
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
}