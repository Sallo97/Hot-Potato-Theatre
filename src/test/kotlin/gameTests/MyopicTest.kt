package gameTests

import org.example.backend.player.MyopicPlayer
import org.example.backend.player.Player
import org.example.backend.potato.Potato
import kotlin.test.Test

class MyopicTest : GameTest() {
    val potato = Potato(10,100, 200)

    @Test
    fun testMyopic1() {
        // All myopic players have equal threshold which is between 0 and the starting lifetime
        val threshold = 8
        val players = mutableSetOf<Player>().apply {
            for(i in 1..10) {
                val player = MyopicPlayer(i, threshold)
                this.add(player)
            }
        }
        doExactGameTest(potato,
            players,
            2,
            2,
            -100,
        )
    }

    @Test
    fun testMyopic2() {
        // Half the player have threshold = 0, others have threshold < starting lifetime
        val thresholdAccept = 0
        val thresholdDeny = 100
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
            5,
            5,
            200,
            true
        )
    }

}