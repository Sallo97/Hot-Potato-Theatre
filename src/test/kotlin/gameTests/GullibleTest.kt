package gameTests

import org.example.player.GulliblePlayer
import org.example.player.Player
import org.example.potato.Potato
import kotlin.test.Test

class GullibleTest: GameTest() {

    @Test
    fun testRational1() {
        val potato = Potato(lifetime = 1000u, gain = 0u, loss = 1000u)
        val players = mutableSetOf<Player>().apply {
            for(i in 1..1000) {
                val player = GulliblePlayer(i)
                this.add(player)
            }
        }
        doGameTest(potato,
            players,
            1000u,
            1000,
            -1000)

    }

    @Test
    fun testRational2() {
        val potato = Potato(lifetime = 100u, gain = 1000u, loss = 0u)
        val players = mutableSetOf<Player>().apply {
            val player = GulliblePlayer(1)
            this.add(player)
        }
        doGameTest(potato,
            players,
            1u,
            1,
            0)

    }
}