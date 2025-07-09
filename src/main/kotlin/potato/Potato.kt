package org.example.potato

/**
 * Defines a hot potato object by three properties:
 * - lifetime: the number of turn in which the hot potato is still valid (i.e. the payoff is a gain)
 * - gain: the payoff for all players that take the hot potato and are not the last player. It is always positive
 * - loss: the payoff for the last player that takes the hot potato. It is always negative
 * We interpret gain and loss value as dollars for sake of simplicity
 */
data class Potato(val lifetime: Int, val gain:Int, val loss: Int)