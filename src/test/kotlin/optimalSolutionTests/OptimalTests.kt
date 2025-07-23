package optimalSolutionTests

import backend.game.Game
import backend.game.GameType
import backend.player.CoalitionalPlayer
import backend.player.GulliblePlayer
import backend.player.Player
import backend.player.PlayerType
import backend.potato.Potato
import backend.potato.PotatoType
import kotlin.test.Test
import kotlin.test.assertEquals

class OptimalTests() {
    @Test
    fun testOptimalGullible() {
        val potato = Potato(5, 5.0, 5.0)
        val players = mutableSetOf<Player>().apply {
            for(i in 1..10) {
                val player = GulliblePlayer(i)
                this.add(player)
            }
        }
        val game = Game(potato, players, GameType.HOMOGENEOUS)
        val solution = game.findOptimalSolution()
        assertEquals(5, solution.size, "solution should have 5 players")
    }

    @Test
    fun testOptimalMixed() {
        val potato = Potato(10, 100.0, 0.5, PotatoType.MUTABLE, 2.0, 0.5)
        val players = mutableSetOf<Player>().apply {
            for(i in 1..2) {
                val player = GulliblePlayer(i)
                this.add(player)
            }
            for(i in 1..3) {
                val player = CoalitionalPlayer(i)
                this.add(player)
            }
        }
        val game = Game(potato, players, GameType.MIXED)
        val solution = game.findOptimalSolution()
        val solutionList = solution.map { it.behavior }
        val expectedList = listOf<PlayerType>(PlayerType.COALITIONAL,PlayerType.COALITIONAL,PlayerType.COALITIONAL,PlayerType.GULLIBLE,PlayerType.GULLIBLE)
        assertEquals(expectedList, solutionList)
    }
}

