package backend.game

import backend.coalition.Coalition
import backend.game.gameStatus.GameStatus
import backend.player.CoalitionalPlayer
import backend.player.GulliblePlayer
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
    private val startingPopulation: Set<Player>,
    val type: GameType) {

    private var status : GameStatus = GameStatus(startingPopulation as MutableSet)
    var numOfCoalitionalPlayer = startingPopulation.filter { it is CoalitionalPlayer }.size
    var coalition: Coalition? = run {
        if(numOfCoalitionalPlayer > 0) { Coalition( this) }
        else { null }
    }

    /**
     * Returns game to starting status.
     */
    private fun reset() {
        status = GameStatus(startingPopulation as MutableSet)
        coalition = run {
            if(numOfCoalitionalPlayer > 0) { Coalition( this) }
            else { null }
        }
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
     * Tries to find an optimal solution, i.e. a sequence of players which will lead to the longest possible chain.
     *
     * @return the ordered list of players representing an optimal solution.
     */
    fun findOptimalSolution() : List<Player> {
        val turns = getRemainingTurnsWithCurrent()

        // Construct acceptance arrays for non-coalitional players
        val nonCoalitionalPlayers = status.activePopulation.filter { player -> player !is CoalitionalPlayer }
        val acceptanceListNotCoalitional: MutableList<AcceptanceArray> = mutableListOf<AcceptanceArray>().apply {
            for (player in nonCoalitionalPlayers) {
                    this.add(constructAcceptanceArray(player, turns))
            }
        }

        // Construct acceptance array for coalitional players
        val coalitionalPlayers = status.activePopulation.filter { player -> player is CoalitionalPlayer }
        val acceptanceListCoalitionalPlayer = constructAcceptanceArraysCoalition(coalitionalPlayers, turns)

        val acceptanceList = mutableListOf<AcceptanceArray>().apply {
            this.addAll(acceptanceListNotCoalitional)
            this.addAll(acceptanceListCoalitionalPlayer)
        }

        val possibleSolution = mutableListOf<Player>().apply {
            for (t in 0..<turns) {
                val currentAcceptPlayers: List<AcceptanceArray> = acceptanceList.filter { it.array[t] }
                val optimalPlayerAtTurn =
                    if(!currentAcceptPlayers.isEmpty()) {
                        findHighestPriorityPlayer(currentAcceptPlayers, t)

                    }
                    else {
                        return emptyList()
                    }
                acceptanceList.remove(optimalPlayerAtTurn)
                this.add(optimalPlayerAtTurn.player)
            }
        }

        return possibleSolution.toList()
    }

    /**
     * @return the first player type with the highest priority over [list].
     */
    private fun findHighestPriorityPlayer(list : List<AcceptanceArray>, turn:Int) : AcceptanceArray {
        return list.minByOrNull { it.acceptanceAfterTurn(turn) }
            ?: throw IllegalArgumentException("List cannot be empty")
    }

    /**
     * @param [turns] number of turns of the game.
     * @return the acceptanceArrays for players in a coalition
     */
    private fun constructAcceptanceArraysCoalition (set: List<Player>, turns: Int) : List<AcceptanceArray> {
        reset()
        val list = mutableListOf<AcceptanceArray>()
        for (player in set) {
            val acceptanceArray = AcceptanceArray(player, turns)
            for( i in getCurrentTurn()..<turns) {
                val acceptance = player.decideAcceptance(this)
                acceptanceArray.array[i] = acceptance
                if(acceptance) {
                    updateGame(player)
                    break
                } else {
                    updateGame(GulliblePlayer(0))
                }
            }
            list.add(acceptanceArray)
        }
        reset()
        return list
    }

    /**
     * @param [turns] number of turns of the game.
     * @return the acceptanceArray of [player] for the following game.
     */
    private fun constructAcceptanceArray (player: Player, turns: Int): AcceptanceArray {
        reset()
        val acceptanceArray = AcceptanceArray(player, turns)
        for(i in 0..<turns) {
            val acceptance = player.decideAcceptance(this)
            acceptanceArray.array[i] = acceptance
            val dummy = GulliblePlayer(0)
            updateGame(dummy)
        }

        reset()
        return acceptanceArray
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