package gameTests

import org.example.player.BarnumPlayer
import org.example.player.Player
import org.example.potato.Potato
import kotlin.random.Random
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

        doExactGameTest(potato,
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

        doExactGameTest(potato,
            players,
            5u,
            5,
            10)
    }

    @Test
    fun testBarnum3() {
        // All players have random prob except one with prob = 1
        val potato = Potato(5u, 5u, 10u)
        val players = mutableSetOf<Player>().apply {
            for (i in 1..4) {
                val randProb = Random.nextDouble(0.0, 1.0 + Double.MIN_VALUE)
                val player = BarnumPlayer(i, randProb)
                this.add(player)
            }
            val player = BarnumPlayer(5, 1.0)
            this.add(player)
        }

        doGEGameTest(potato,
            players,
            1u,
            1,
        -10,
            true)
    }
}