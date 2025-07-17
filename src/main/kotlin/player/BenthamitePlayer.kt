package org.example.player

import org.example.game.Game
import kotlin.math.pow

/**
 * A Benthamite player in the SHPG, i.e. an irrational player not concerned with their own payoff, rather it is focused
 * on maximizing the total payoff of the game. Still, if during a decision, it is aware that other players could take the
 * risk, then it could reject the potato.
 *
 * @property [alterAcceptBelief] the belief of the current player that an alter will accept the potato at the current
 * turn. Is a value in [0,1]. Default value is 1/2.
 * @property [payoff] the payoff of the player.
 * @constructor creates a player without the hot potato, with a [payoff] of 0 and with [alterAcceptBelief] passed as argument.
 */
class BenthamitePlayer(id: Int, val alterAcceptBelief: Double = 0.5) : Player(id){
    init {
        require(alterAcceptBelief in 0.0..1.0)
    }

    override fun decideAcceptance(game: Game): Boolean {
        val potato = game.potato
        val remainingTurns = minOf(potato.lifetime - game.turn, game.activePopulation.size.toUInt() - 1u)

        val probAnotherAlterWillAccept = alterAcceptBelief.pow(remainingTurns.toInt())

        val gainWeight = (potato.gain.toDouble() * remainingTurns.toDouble() * (1 - probAnotherAlterWillAccept)) - potato.loss.toDouble()
        val lossWeight = potato.loss.toDouble()

        val decision = gainWeight > lossWeight
        return decision
    }
}