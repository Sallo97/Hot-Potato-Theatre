package backend.game

import backend.player.Player

/**
 * Represents for a given game a boolean [array] indicating for each entry at position i if [player] would accept the
 * potato at turn i.
 *
 * @param [totNumOfTurns] is the total number of turns of the game (i.e. the size of [array]).
 *
 */
class AcceptanceArray(val player: Player, totNumOfTurns:Int) {
    val array = MutableList(totNumOfTurns) { false }

    /**
     * @return the number of entries after the first [numOfTurns] set to true.
     */
    fun acceptanceAfterTurn(numOfTurns: Int) : Int {
        return array.drop(numOfTurns).count { it }
    }
}