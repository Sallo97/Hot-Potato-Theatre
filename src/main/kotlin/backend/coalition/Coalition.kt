package backend.coalition

import backend.game.Game
import backend.game.GameType
import backend.player.CoalitionalPlayer
import backend.player.Player
import backend.potato.Potato
import kotlin.math.pow

/**
 * Implements a basic coalition between coalitional players, managing all the surrounding information.
 *
 * @property [payoff] the current payoff guaranteed for the current players in the coalition.
 */
class Coalition (
    game: Game) {
    private var gain: Double = 0.0
    var payoff: Double? = null

    private data class Member (val player: Player, val turn: Int)
    private val chain: MutableList<Member> = mutableListOf()

    private class OptimalCoalition (val optimalSize:Int, val optimalPayoff: Double)
    private val optimalCoalition: OptimalCoalition? = if(game.type == GameType.HOMOGENEOUS) {
        findBestCoalition(game.potato, game.numOfCoalitionalPlayer)
    } else {
        null
    }

    /**
     * @return true if the coalition is willing to take a new member, false otherwise.
     */
    fun isWillingToTakeANewMember() : Boolean {
        val decision = optimalCoalition?.let{
            chain.size < optimalCoalition.optimalSize
        }?: true

        return decision
    }

    /**
     * @return the optimal size of the coalition and optimal payoff possible according to [potato].
     */
    private fun findBestCoalition(potato: Potato, numOfCoalitionalPlayer: Int): OptimalCoalition? {
        val turns = minOf(potato.lifetime, numOfCoalitionalPlayer)

        data class State(
            val gain: Double,
            val loss: Double,
            val optimalCoalition: OptimalCoalition
        )

        val finalState = (1..turns).fold(
            State(0.0, potato.baseLoss, OptimalCoalition(0, Double.NEGATIVE_INFINITY))
        ) { state, i ->
            val currentPayoff = (state.gain - state.loss) / i
            val nextGain = state.gain + if (state.gain == 0.0) potato.baseGain else potato.gainFactor * state.gain
            val nextLoss = state.loss * potato.lossFactor

            if (currentPayoff > state.optimalCoalition.optimalPayoff) {
                State(nextGain, nextLoss, OptimalCoalition(i, currentPayoff))
            } else {
                State(nextGain, nextLoss, state.optimalCoalition)
            }
        }

        if (finalState.optimalCoalition.optimalPayoff < 0) {
            return null
        }
        return finalState.optimalCoalition
    }

    /**
     * After game ended provide the payoff to all players which engaged in the coalition.
     * The amount of payoff is equal to all members of the coalition.
     */
    fun distributePayoff() {
        for (member in chain) {
            member.player.payoff += payoff?:error("payoff of the coalition is null")
        }
    }

    /**
     * Adds [player] at [turn] to the coalition.
     */
    fun addMember(player: CoalitionalPlayer, turn: Int, potato: Potato) {
        optimalCoalition?.let {
            require(isWillingToTakeANewMember()) {"The coalition is full, no other members can be added"}
        }
        chain.add(Member(player, turn))

        // Updating gain and payoff
        if(chain.size != 1) {
            val gainTurn = chain[chain.size - 2].turn
            gain = gain.plus(potato.gainFactor.pow(gainTurn) * potato.baseGain)
        }
        val  lossTurn = chain.last().turn
        val totalPayoff = gain.minus((potato.baseLoss * potato.lossFactor.pow(lossTurn)))
        payoff = totalPayoff / chain.size
    }

    /**
     * @return the number of players in the coalition.
     */
    fun size(): Int {
        return chain.size
    }

    override fun toString(): String {
        val message = if (chain.isEmpty()) {
            "No coalition was formed"
        } else {
            "Coalition = { coalition payoff = $payoff; number of members = ${chain.size}, members = [$chain] }"
        }
        return message
    }

    /**
     * [proposerLoss] the loss the proposer is willing to take.
     * @return true if the coalition accept the proposer to be a member, false otherwise.
     */
    fun acceptProposition(proposerLoss: Double) : Boolean {
        val proposedPayoff = (gain - proposerLoss)/ (chain.size - 1)
        val decision = payoff?.let{ proposedPayoff >= it }?:true
        return decision
    }

}