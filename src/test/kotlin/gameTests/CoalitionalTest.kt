package gameTests

import backend.player.CoalitionalPlayer
import org.example.backend.player.Player
import org.example.backend.potato.Potato
import kotlin.test.Test

class CoalitionalTest: GameTest() {

    @Test
    fun testCoalitional1() {
        val potato = Potato(lifetime = 10, gain = 5.0, loss = 10.0)
        val players = mutableSetOf<Player>().apply {
            for(i in 1..10) {
                val player = CoalitionalPlayer(i, 0.5)
                this.add(player)
            }
        }
        doExactGameTest(potato,
            players,
            10,
            10,
            35.0)

    }
}