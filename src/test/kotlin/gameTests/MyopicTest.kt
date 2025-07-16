package gameTests

import org.example.player.MyopicPlayer
import org.example.player.Player
import org.example.potato.Potato
import kotlin.test.Test

class MyopicTest : GameTest() {

    @Test
    fun testMyopic1() {
        // All myopic players have equal threshold which is between 0 and the starting lifetime
        val potato = Potato(10u,100u, 200u)
        val threshold = 8u
        val players = mutableSetOf<Player>().apply {
            for(i in 1..10) {
                val player = MyopicPlayer(i, threshold)
                this.add(player)
            }
        }
        doExactGameTest(potato,
            players,
            2u,
            2,
            -100,
        )
    }

    @Test
    fun testMyopic2() {
        // Half the player have threshold = 0, others have threshold < starting lifetime
        val potato = Potato(10u,100u, 200u)
        val thresholdAccept = 0u
        val thresholdDeny = 100u
        val players = mutableSetOf<Player>().apply {
            for(i in 1..5) {
                val player = MyopicPlayer(i, thresholdAccept)
                this.add(player)
            }
            for(i in 1..5) {
                val player = MyopicPlayer(i, thresholdDeny)
                this.add(player)
            }
        }
        doExactGameTest(potato,
            players,
            5u,
            5,
            200,
            true
        )
    }

}