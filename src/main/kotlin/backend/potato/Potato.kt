package backend.potato

import backend.player.Player

/**
 * Defines a hot potato object.
 *
 * @property [lifetime] the number of turn in which the hot potato is still valid (i.e. the payoff is a gain).
 * @property [baseGain] the payoff for all players that take the hot potato and are not the last player. It is always positive.
 * @property [baseLoss] the payoff for the last player that takes the hot potato. It is always negative.
 * @property [type] specifies if the potato is fixed or mutable.
 * @property [gainFactor] by how much the gain changes over a turn.
 * @property [baseLoss] by how much the loss changes over a turn.
 * @property [ratio] the positive ratio between the current gain and loss (i.e. gain / (-loss)).
 * @property [currentHolder] the current player holding the potato. At the start no one holds it.
 * We interpret gain and loss value as dollars for sake of simplicity
 */
data class Potato(val lifetime:Int, val baseGain:Double, val baseLoss:Double,
                  val type: PotatoType = PotatoType.FIXED, val gainFactor: Double = 1.0, val lossFactor: Double = 1.0) {
    var currentGain = baseGain
    var currentLoss = baseLoss
    var ratio : Double = currentGain/currentLoss
    var currentHolder: Player? = null

    init {
        if(type == PotatoType.FIXED) {
            require(gainFactor == 1.0) {"A fixed potato cannot have mutable gain"}
            require(lossFactor == 1.0) {"A fixed potato cannot have mutable loss"}
        }
        require(baseGain >= 0) {"gain must be represented as an absolute value"}
        require(baseLoss >= 0) {"gain must be represented as an absolute value"}
        require(gainFactor > 0) {"gainFactor must be strictly positive"}
        require(lossFactor > 0) {"gainFactor must be strictly positive"}
    }

    override fun toString(): String {
        return "{ type: ${type.name}; lifetime: $lifetime; gain: $baseGain; gainFactor: $gainFactor; loss: $baseLoss; lossFactor: $lossFactor }"
    }

    /**
     * Gives the according payoff depending on if the player is the last one or not.
     *
     * @param [isLastPlayer] flag passed by a player to specify if it is the last player or not.
     * @return the player's payoff.
     */
    fun getPayoff (isLastPlayer: Boolean = false) : Double {

        return if (!isLastPlayer) {
            currentGain
        } else {
            -currentLoss
        }
    }

    /**
     * Updates the potato gain and loss after a turn by their factors.
     */
    fun updatePotato () {
        currentGain *= gainFactor
        currentLoss *= lossFactor
        ratio = currentGain / currentLoss
    }
}