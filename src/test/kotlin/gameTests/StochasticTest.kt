package gameTests

import org.example.player.Player
import org.example.player.StochasticPlayer
import org.example.potato.Potato
import kotlin.test.Test

class StochasticTest : GameTest() {
    val potato = Potato(10, 100, 200)


    @Test
    fun testStochastic1() {
        // All stochastic players have equal weight which is 0 (thus all will accept the potato)
        val weight = 0.0
        val players = mutableSetOf<Player>().apply {
            for(i in 1..5) {
                val player = StochasticPlayer(i, weight)
                this.add(player)
            }
        }
        doExactGameTest(potato,
            players,
            5,
            5,
            200)
    }

    @Test
    fun testStochastic2() {
        // All stochastic players have equal weight which is 1 (thus all will deny the potato)
        val weight = 1.0
        val players = mutableSetOf<Player>().apply {
            for(i in 1..5) {
                val player = StochasticPlayer(i, weight)
                this.add(player)
            }
        }
        doExactGameTest(potato,
            players,
            0,
            0,
            0)
    }
}