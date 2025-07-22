package backend.game

import backend.coalition.Coalition
import backend.gameStatus.GameStatus
import backend.player.CoalitionalPlayer
import backend.player.Player
import backend.potato.Potato

/**
 * Implements the Simple Hot Potato Game.
 *
 * @param[startingPopulation] the set of players of the game.
 * @property [potato] the hot potato good associated to the game.
 * @property [coalition] the active coalition of coalitional players in the game.
 * @property [status] keeps track of the state of the game.
 * @property [type] the type of game (Homogeneous or Mixed).
 * @constructor creates a game with the given [potato] and population.
 */
class Game (
    val potato: Potato,
    startingPopulation: Set<Player>,
    val type: GameType) {

    private val status : GameStatus = GameStatus(startingPopulation as MutableSet)
    val numOfCoalitionalPlayer = startingPopulation.filter { it is CoalitionalPlayer }.size
    val coalition: Coalition? = run {
        if(numOfCoalitionalPlayer > 0) { Coalition( this) }
        else { null }
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
        coalition?.distributePayoff()
        status.gameEnded = true
    }

    /**
     * @return true if the game ended, false otherwise.
     */
    fun isGameEnded() : Boolean {
        return  status.gameEnded
    }

    /**
     * @return the final state of the game (total payoff, final chain, etc...).
     */
    override fun toString () : String {
        var str = if (!isGameEnded()) {
            "Ongoing game with:\n" +
                    "- current turn = ${getCurrentTurn()}\n" +
                    "- turns left (not counting current) = ${getRemainingTurnsExceptCurrent()}\n"
        } else {
            "Game ended with:\n" +
                    "- total number of turns = ${getCurrentTurn()}\n"
        }
        str +=  "- Total payoff = ${status.totalPayoff}\n" +
                "- ${status.chainToString()}\n" +
                "- Coalition = $coalition\n" +
                "- type = $type\n"

        return str
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
     * @return the number of turns left counting also the current one. Must be a value >= 0.
     */
    fun getRemainingTurnsWithCurrent() : Int {
        var result = minOf(potato.lifetime - status.turn, getNumOfAvailablePlayers())
        result = maxOf(result, 0)
        return result
    }

    /**
     * @return the number of turns left not counting the current one.
     */
    fun getRemainingTurnsExceptCurrent() : Int {
        val result = maxOf(getRemainingTurnsWithCurrent() - 1, 0)
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
    fun getTotalPayoff() : Double {
        return status.totalPayoff
    }

    /**
     * Updates the game status.
     *
     * @param [p] the actor who decided to accept the good at the current turn.
     */
    private fun updateGame (p: Player) {
        status.updateStatus(p)
        potato.updatePotato()
        if (p is CoalitionalPlayer) {
            coalition!!.addMember(p, status.turn, potato)
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