package backend.player

import backend.game.Game

/**
 * Represents an irrational player in the SHPG, i.e. one which will always take the good if asked.
 *
 * @property [id] unique identifier for the player.
 * @property [payoff] the payoff of the player. At the start it is always 0.
 * @constructor creates a player without the hot potato and with a payoff of 0.
 */
class GulliblePlayer (id: Int) : Player(id) {

    /**
     * Handles the decision logic of the irrational player in taking or not the good.
     *
     * @return true, since an irrational player will always accept a hot potato good.
     */
    override fun decideAcceptance(game: Game): Boolean {
        return true
    }
}