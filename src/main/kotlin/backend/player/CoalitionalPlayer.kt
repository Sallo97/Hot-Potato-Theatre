package backend.player

import backend.game.Game

/**
 * A Coalitional player in the SHPG, i.e. an irrational player concerned in forming a coalition with other players of the
 * same type in order to get a better payoff.
 *
 * @property [payoff] the payoff of the player.
 * @constructor creates a player without the hot potato, with a [payoff] of 0.
 */
class CoalitionalPlayer(id:Int) : Player(id, PlayerType.COALITIONAL) {

    /**
     * Handles the decision logic of the player behind either the acceptance or denying of the hot potato.
     * The decision of a Coalition player depends on the coalition. If the coalition has room left for him/her
     * in order to reach the optimalPayoff and said value is positive, then the player will accept, otherwise it
     * will deny the good.
     *
     * @param [game] represents the current game state, which impacts the choice.
     * @return true if the player *chosen* to accept the good, false otherwise.
     */
    override fun decideAcceptance(game: Game): Boolean {
        val coalition = game.coalition!!
        return (coalition.spaceLeft > 0) && (coalition.optimalPayoff > 0.0)
    }

    override fun toString(): String {
        val str = super.toString()
        return str
    }
}
