package org.example.player

import org.example.game.Game

/**
 * Represents a player whose rationality depends on [threshold]: if the current lifetime of the potato is greater than
 * [threshold] then it plays like a Gullible player (i.e. takes always the good), otherwise it plays rationally (never
 * gets the good).
 *
 * @property [id] unique identifier for the player.
 * @property [payoff] the payoff of the player. At the start it is always 0.
 * @property [threshold] determines how the player sees the current chain of the game. if the remaining number of turns
 * of the game is greater than [threshold], then it sees it as an infinite chain, otherwise it sees the end.
 * @constructor creates a player without the hot potato and with a payoff of 0.
 */
class MyopicPlayer(id: Int, val threshold: Int) : Player(id) {

    /**
     * Handles the decision logic of the player behind either the acceptance or denying of the hot potato.
     * The decision of Myopic player depends on the state of the lifetime of the potato and the [threshold]:
     * - if min (potato's lifetime, remaining players) > [threshold] then the player will accept the hotpotato
     * - otherwise false
     * @param [game] represents the current game state, which impacts the choice.
     * @return true if the player *chosen* to accept the good, false otherwise.
     */
    override fun decideAcceptance(game: Game): Boolean {
        val turn = game.getCurrentTurn()
        val remainingTurns = minOf(game.potato.lifetime - turn, game.getNumOfAvailablePlayers())

        val decision = remainingTurns > threshold
        return decision
    }

    override fun toString(): String {
        return "{id: $id; payoff: $payoff; threshold: $threshold}"
    }


}