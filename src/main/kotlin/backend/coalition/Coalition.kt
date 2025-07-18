package org.example.backend.coalition

import org.example.backend.player.CoalitionalPlayer

/**
 * Implements a basic coalition between coalitional players, managing all the surrounding information.
 *
 * @property [totalCoalitionalPlayers] the total number of coalitional players in the game.
 * @property [members]  set of coalitional players which are in the coalition.
 * @property [totalPayoff] total payoff gained by the coalition thus far.
 * @property [possibleMembers] the number of coalitional players not yet in the coalition.
 */
data class Coalition (
    val totalCoalitionalPlayers: UInt) {

    var totalPayoff : Double = 0.0
    private val members: MutableSet<CoalitionalPlayer> = mutableSetOf()
    var possibleMembers = totalCoalitionalPlayers

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

     fun addMember(p: CoalitionalPlayer) {
        members.add(p)
        possibleMembers--
    }

    /**
     * @return the number of players in the coalition.
     */
    fun size(): Int {
        return members.size
    }

    override fun toString(): String {
        val membersStr = if (members.isEmpty()) {
            "∅"
        } else {
            members.toString()
        }
        val msg = "Coalition = { coalition payoff = $totalPayoff; number of members = ${members.size}, members = $membersStr }"
        return msg
    }

}