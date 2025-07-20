package backend.player

import backend.game.Game

/**
 * A Benthamite player in the SHPG, i.e. an irrational player not concerned with their own payoff, rather it is focused
 * on maximizing the total payoff of the game. Still, if during a decision, it is aware that other players could take the
 * risk, then it could reject the potato.
 *
 * @property [gainWeight] how much weight has the acceptance of the hot potato. Is a value > 0.
 * @property [lossWeight] how much weight has the loss of the hot potato. Is a value > 0.
 * @property [payoff] the payoff of the player.
 * @constructor creates a player without the hot potato, with a [payoff] of 0 and with [gainWeight] and [lossWeight] set
 * as hyperparameters.
 */
class BenthamitePlayer(
    id: Int,
    val gainWeight: Double = 0.5,
    val lossWeight: Double = 0.5) : Player(id, PlayerType.BENTHAMITE){
    init {
        require(gainWeight in 0.0..1.0)
    }

    /**
     * Handles the decision logic of the player behind either the acceptance or denying of the hot potato.
     * The decision of a Benthamite player depends on how much the player is willing to increase the total payoff.
     *
     * @param [game] represents the current game state, which impacts the choice.
     * @return true if the player *chosen* to accept the good, false otherwise.
     */
    override fun decideAcceptance(game: Game): Boolean {
        val potato = game.potato
        val remainingTurns = game.getRemainingTurnsExceptCurrent()
        val remainingPlayers = game.getNumOfAvailablePlayers() - 1 // do not consider current player

        val groupBenefit: Double = gainWeight * (potato.currentGain * remainingTurns - potato.currentLoss)

        val diffusionResponsibility: Double = if (remainingPlayers == 0) {
            0.0
        } else {
            lossWeight * (1/remainingPlayers)
        }

        return potatoAcceptance(groupBenefit, diffusionResponsibility, potato)
    }

    override fun toString(): String {
        val str = "${super.toString()} alterAcceptBelief: $gainWeight}"
        return str
    }
}