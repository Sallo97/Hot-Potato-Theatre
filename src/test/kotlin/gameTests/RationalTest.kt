package gameTests

import backend.player.Player
import backend.player.RationalPlayer
import backend.potato.Potato
import kotlin.test.Test

class RationalGame : GameTest(){

    @Test
    fun testRational1() {
        val potato = Potato(lifetime = 100, baseGain = 100.0, baseLoss = 100.0)
        val players = mutableSetOf<Player>().apply {
            for(i in 1..10) {
                val player = RationalPlayer(i)
               this.add(player)
            }
        }
        doExactGameTest(potato,
            players,
            0,
            0,
            0.0)

    }

    @Test
    fun testRational2() {
        val potato = Potato(lifetime = 0, baseGain = 0.0, baseLoss = 1000.0)
        val players = mutableSetOf<Player>().apply {
            val player = RationalPlayer(1)
            this.add(player)
        }
        doExactGameTest(potato,
            players,
            0,
            0,
            0.0)

    }
}