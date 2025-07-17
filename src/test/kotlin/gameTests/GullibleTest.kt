package gameTests

import org.example.backend.player.GulliblePlayer
import org.example.backend.player.Player
import org.example.backend.potato.Potato
import kotlin.test.Test

class GullibleTest: GameTest() {

    @Test
    fun testGullible1() {
        val potato = Potato(lifetime = 5, gain = 0, loss = 1000)
        val players = mutableSetOf<Player>().apply {
            for(i in 1..5) {
                val player = GulliblePlayer(i)
                this.add(player)
            }
        }
        doExactGameTest(potato,
            players,
            5,
            5,
            -1000)

    }

    @Test
    fun testGullible2() {
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