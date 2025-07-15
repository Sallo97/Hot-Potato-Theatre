package gameTests

import org.example.player.BarnumPlayer
import org.example.player.Player
import org.example.potato.Potato
import kotlin.test.Test

class BarnumGameTest: GameTest() {

    @Test
    fun testBarnum1() {
        // All players have prob = 1, meaning they behave as rational players.
        val potato = Potato(lifetime = 5u, gain = 5u, loss = 10u)
        val players = mutableSetOf<Player>().apply {
            for (i in 1..10) {
                val player = BarnumPlayer(i, prob = 1.0)
                this.add(player)
            }
        }

        doGameTest(potato,
            players,
            0u,
            0,
            0)
    }

    @Test
    fun testBarnum2() {
        // All players have prob = 0, meaning they behave as gullible players.
        val potato = Potato(lifetime = 5u, gain = 5u, loss = 10u)
        val players = mutableSetOf<Player>().apply {
            for (i in 1..10) {
                val player = BarnumPlayer(i, prob = 0.0)
                this.add(player)
            }
        }

        doGameTest(potato,
            players,
            5u,
            5,
            10)
    }

    


}