package gameTests

import backend.player.GulliblePlayer
import backend.player.Player
import backend.potato.Potato
import kotlin.test.Test

class GullibleTest: GameTest() {

    @Test
    fun testGullible1() {
        val potato = Potato(lifetime = 5, baseGain = 0.0, baseLoss = 1000.0)
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
            -1000.0)

    }

    @Test
    fun testGullible2() {
        val potato = Potato(lifetime = 100, baseGain = 1000.0, baseLoss = 0.0)
        val players = mutableSetOf<Player>().apply {
            val player = GulliblePlayer(1)
            this.add(player)
        }
        doExactGameTest(potato,
            players,
            1,
            1,
            0.0)

    }
}