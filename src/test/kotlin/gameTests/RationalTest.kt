package gameTests

import org.example.backend.player.Player
import org.example.backend.player.RationalPlayer
import org.example.backend.potato.Potato
import kotlin.test.Test

class RationalGame : GameTest(){

    @Test
    fun testRational1() {
        val potato = Potato(lifetime = 100, gain = 100.0, loss = 100.0)
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
        val potato = Potato(lifetime = 0, gain = 0.0, loss = 1000.0)
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