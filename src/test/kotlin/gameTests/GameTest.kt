package gameTests

import org.example.game.Game
import org.example.player.Player
import org.example.potato.Potato
import kotlin.test.assertEquals

/**
 * Generic class that defines function to create tests on games.
 */
open class GameTest {

    /**
     * Does a test on the specified game.
     *  * @property [potato] the hot potato of the game.
     *  * @property [players] the initial population of the game.
     *  * @property [expTurn] the expected value of turn after the game's end.
     *  * @property [expChainSize] the expected chain of players after the game's end.
     *  * @property [expTotalPayoff] the expected total payoff after the game's end.
     */
    fun doGameTest(potato: Potato,
                   players: MutableSet<Player>,
                   expTurn: UInt,
                   expChainSize: Int,
                   expTotalPayoff:Int  ) {
        val game = Game(potato, players)
        game.run()

        assertEquals(expTurn, game.turn, "turn (${game.turn}) !=  expected($expTurn)")
        assertEquals(expChainSize, game.getChainSize(), "generated chain's size: ${game.getChainSize()}\ndiffers from " +
                "expected one: $expChainSize")
        assertEquals(expTotalPayoff, game.totalPayoff, "totalPayoff(${game.totalPayoff} != " +
                "expected($expTotalPayoff)")
    }
}