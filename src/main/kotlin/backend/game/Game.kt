package org.example.backend.game

import org.example.backend.coalition.Coalition
import org.example.backend.gameStatus.GameStatus
import org.example.backend.player.CoalitionalPlayer
import org.example.backend.player.Player
import org.example.backend.potato.Potato

/**
 * Implements the Simple Hot Potato Game.
 *
 * @param[startingPopulation] the set of players of the game.
 * @property [potato] the hot potato good associated to the game.
 * @property [coalition] the active coalition of coalitional players in the game.
 * @property [status] keeps track of the state of the game.
 * @constructor creates a game with the given [potato] and population.
 */
class Game (
    val potato: Potato,
    startingPopulation: Set<Player>) {

    val coalition: Coalition
    private val status : GameStatus = GameStatus(startingPopulation as MutableSet)

    init {
        val coalitionSetSize = startingPopulation.filter { it is CoalitionalPlayer }.size.toUInt()
        coalition = Coalition(coalitionSetSize)
    }

    /**
     * Handles the game execution from start to end.
     * @param [tryAll] if true for a willing player to take the potato check over all the set of available players,
     * otherwise will check only a single random one.
     */
    fun run(tryAll: Boolean = true) {
        var foundNewHolder = findingWillingPlayer(tryAll)

        while(foundNewHolder) {
            val holder = potato.currentHolder!!
            updateGame(holder)

            foundNewHolder = findingWillingPlayer(tryAll)
            givePayoff(holder, !foundNewHolder)
        }

        // Game Ended, let the coalition split their total payoff among the members
        coalition.splitTotalPayoff()
    }

    /**
     * @return the final state of the game (total payoff, final chain, etc...).
     */
    fun getEndGameInfo () : String {
        val str = "Game ended with the following:\n" +
                "- Potato's lifetime = ${potato.lifetime}\t turns = ${status.turn}\n" +
                "- ${status.chainToString()}\n"
        "- Total payoff = ${status.totalPayoff}\n"
        return str
    }

    override fun toString(): String {
        return "{ turn: ${status.turn}; numOfPlayers: ${status.numOfPlayers}; " +
                "numOfRemainingPlayers: ${status.activePopulation.size} }"
    }

    /**
     * @return true if the [potato] has not reached its end-of-life, false otherwise
     */
    fun isPotatoAlive () : Boolean{
        if (potato.lifetime < status.turn) {
            error("ERROR! Potato's lifetime (${potato.lifetime}) < current turn ($status.turn).\n The current turn  cannot " +
                    "exceed the potato's lifetime ")
        }
        return potato.lifetime > status.turn
    }

    /**
     * @return the number of players available for getting the potato.
     */
    fun getNumOfAvailablePlayers(): Int {
        return status.activePopulation.size
    }

    /**
     * @return the current turn of the play.
     */
    fun getCurrentTurn() : Int {
        return status.turn
    }

    /**
     * @return the number of turns left not counting current one before the end of the game.
     */
    fun getRemainingTurnsWithCurrent() : Int {
        val result = minOf(potato.lifetime - status.turn, getNumOfAvailablePlayers())
        return result
    }

    /**
     * @return the number of turns left not counting current one before the end of the game.
     */
    fun getRemainingTurnsExceptCurrent() : Int {
        val result = getRemainingTurnsWithCurrent() - 1
        return result
    }

    /**
     * @return the size of the chain of players that partake in the game.
     */
    fun getChainSize() : Int {
        return status.chain.size
    }

    /**
     * @return the sum of all player payoff
     */
    fun getTotalPayoff() : Int {
        return status.totalPayoff
    }

    /**
     * Updates the game status.
     *
     * @param [p] the actor who decided to accept the good at the current turn.
     */
    private fun updateGame (p: Player) {
        status.updateStatus(p)
        if (p is CoalitionalPlayer) {
            coalition.addMember(p)
        }
    }

    /**
     * Gives the payoff to player [p] and updates game status. Be aware that if [p] is a coalitional player, then it
     * gives the payoff to the coalition instead.
     * @param [isLastPlayer] specifies of if [p] is the last player or not.
     */
     private fun givePayoff(p:Player, isLastPlayer: Boolean = false) {
        val payoff = potato.getPayoff(isLastPlayer)
        status.totalPayoff += payoff

        if (p !is CoalitionalPlayer) {
            p.payoff += payoff
        } else {
            coalition.totalPayoff += payoff
        }
    }

    /**
     * Search among the active population (i.e. those that did not already take the hot potato), the first one willing to
     * take the good.
     *
     * @param [askAll] if true check over all the active set for a willing player, otherwise will check only a single random
     * player.
     * @return true if a new player to exchange the good was found, false otherwise.
     */
    private fun findingWillingPlayer(askAll: Boolean) : Boolean {
        if (getRemainingTurnsWithCurrent() <= 0) {
            return false
        }

        val candidates = if (askAll) {
            status.activePopulation.asSequence()
        } else {
            sequenceOf(status.activePopulation.random())
        }

        val willingPlayer = candidates.firstOrNull { it.decideAcceptance(this) }

        val result = if(willingPlayer != null) {
            potato.currentHolder = willingPlayer
            true
        } else {
            false
        }
        return result
    }
}