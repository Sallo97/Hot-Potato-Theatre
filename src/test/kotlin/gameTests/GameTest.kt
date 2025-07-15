package gameTests

import org.example.game.Game
import org.example.player.Player
import org.example.potato.Potato
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Generic class that defines function to create tests on games.
 */
open class GameTest {

    /**
     * Does a test, checking that the expected values are exactly those of the specified game.
     *  @property [potato] the hot potato of the game.
     *  @property [players] the initial population of the game.
     *  @property [expTurn] the expected value of turn after the game's end.
     *  @property [expChainSize] the expected chain of players after the game's end.
     *  @property [expTotalPayoff] the expected total payoff after the game's end.
     *  @param [tryAll] if true for finding the starting player check over all the set for a willing player, otherwise
     *  will check only a single random player.
     */
    fun doExactGameTest(potato: Potato,
                        players: MutableSet<Player>,
                        expTurn: UInt,
                        expChainSize: Int,
                        expTotalPayoff:Int,
                        tryAll: Boolean = false) {
        val game = Game(potato, players)
        game.run(tryAll)

        assertEquals(expTurn, game.turn, "turn (${game.turn}) !=  expected($expTurn)")
        assertEquals(expChainSize, game.getChainSize(), "generated chain's size(${game.getChainSize()})\ndiffers from " +
                "expected one($expChainSize)")
        assertEquals(expTotalPayoff, game.totalPayoff, "totalPayoff(${game.totalPayoff} != " +
                "expected($expTotalPayoff)")
    }

    /**
     * Does a test, checking that the expected values are Greater or Equal of those from the specified game.
     * @property [potato] the hot potato of the game.
     * @property [players] the initial population of the game.
     * @property [expTurn] the expected value of turn after the game's end.
     * @property [expChainSize] the expected chain of players after the game's end.
     * @property [expTotalPayoff] the expected total payoff after the game's end.
     * @param [tryAll] if true for finding the starting player check over all the set for a willing player, otherwise
     * will check only a single random player.
     */
    fun doGEGameTest(potato: Potato,
                      players: MutableSet<Player>,
                      expTurn: UInt,
                      expChainSize: Int,
                      expTotalPayoff:Int,
                      tryAll: Boolean = false) {
        val game = Game(potato, players)
        game.run(tryAll)

        assertTrue(game.turn >= expTurn, "turn (${game.turn}) isn't >=  expected($expTurn)")
        assertTrue(game.getChainSize() >= expTotalPayoff, "generated chain's size(${game.getChainSize()}) isn't >= " +
                "expected one($expChainSize")
        assertTrue(game.totalPayoff >= expTotalPayoff, "totalPayoff(${game.totalPayoff} isn't >= " +
                "expected($expTotalPayoff)")

    }
}