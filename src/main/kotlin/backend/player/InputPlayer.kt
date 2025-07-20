package backend.player

import backend.game.Game
import frontend.ui.choiceFromStdin

/**
 * An Input player in the SHPG, i.e. an irrational player whose decision depends on the user input.
 *
 * @property [payoff] the payoff of the player.
 * @constructor creates a player without the hot potato, with a [payoff] of 0.
 */
class InputPlayer(id: Int) : Player(id, PlayerType.INPUT) {
    override fun decideAcceptance(game: Game): Boolean {
        val choice = choiceFromStdin("accept the hot potato")
        return choice
    }

    override fun toString(): String {
        return "${super.toString()} }"
    }

}