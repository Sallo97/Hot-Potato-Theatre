package gameTests

import org.example.player.GulliblePlayer
import org.example.player.Player
import org.example.potato.Potato
import kotlin.test.Test

class GullibleTest: GameTest() {

    @Test
    fun testRational1() {
        val potato = Potato(lifetime = 1000, gain = 0, loss = 1000)
        val players = mutableSetOf<Player>().apply {
            for(i in 1..1000) {
                val player = GulliblePlayer(i)
                this.add(player)
            }
        }
        doExactGameTest(potato,
            players,
            1000,
            1000,
            -1000)

    }

    @Test
    fun testRational2() {
        val potato = Potato(lifetime = 100, gain = 1000, loss = 0)
        val players = mutableSetOf<Player>().apply {
            val player = GulliblePlayer(1)
            this.add(player)
        }
        doExactGameTest(potato,
            players,
            1,
            1,
            0)

    }
}