package backend.coalition

import backend.player.CoalitionalPlayer
import backend.potato.Potato

/**
 * Implements a basic coalition between coalitional players, managing all the surrounding information.
 *
 * @property [totalCoalitionalPlayers] the total number of coalitional players in the game.
 * @property [members]  set of coalitional players which are in the coalition.
 * @property [totalPayoff] total payoff gained by the coalition thus far.
 * @property [spaceLeft] the number of coalitional players not yet in the coalition.
 */
class Coalition (
    val totalCoalitionalPlayers: Int,
    potato : Potato) {

    var totalPayoff : Double = 0.0
    private val members: MutableSet<CoalitionalPlayer> = mutableSetOf()
    data class BestCoalition(
        val bestSize: Int,
        val bestPayoff: Double
    )
    private val bestCoalition: BestCoalition = findOptimalPayoffAndSize(potato)
    var spaceLeft = bestCoalition.bestSize
    val optimalPayoff = bestCoalition.bestPayoff


    /**
     * @return the optimal size of the coalition and optimal payoff possible according to [potato].
     */
    private fun findOptimalPayoffAndSize(potato: Potato): BestCoalition {
        val turns = minOf(potato.lifetime, totalCoalitionalPlayers)

        data class State(
            val gain: Double,
            val loss: Double,
            val bestCoalition: BestCoalition
        )

        val finalState = (1..turns).fold(
            State(0.0, potato.baseLoss, BestCoalition(0, Double.NEGATIVE_INFINITY))
        ) { state, i ->
            val currentPayoff = (state.gain - state.loss) / i
            val nextGain = state.gain + if (state.gain == 0.0) potato.baseGain else potato.gainFactor * state.gain
            val nextLoss = state.loss * potato.lossFactor

            if (currentPayoff > state.bestCoalition.bestPayoff) {
                State(nextGain, nextLoss, BestCoalition(i, currentPayoff))
            } else {
                State(nextGain, nextLoss, bestCoalition)
            }
        }

        if (finalState.bestCoalition.bestPayoff < 0) {
            return BestCoalition(0, Double.NEGATIVE_INFINITY)
        }
        return finalState.bestCoalition
    }

    /**
     * After game ended provide the payoff to all players which engaged in the coalition.
     * The amount of payoff is equal to all members of the coalition.
     */
    fun splitTotalPayoff() {
        val payoff: Double = totalPayoff / members.size
        for (p in members) {
            p.payoff += payoff
        }
    }

    /**
     * Adds [p] to the coalition
     */
    fun addMember(p: CoalitionalPlayer) {
        require(spaceLeft > 0) {"The coalition is full, no other members can be added"}
        members.add(p)
        spaceLeft--
    }

    /**
     * @return the number of players in the coalition.
     */
    fun size(): Int {
        return members.size
    }

    override fun toString(): String {
        val message = if (members.isEmpty()) {
            "No coalition was formed"
        } else {
            "Coalition = { coalition payoff = $totalPayoff; number of members = ${members.size}, members = [$members] }"
        }
        return message
    }

}