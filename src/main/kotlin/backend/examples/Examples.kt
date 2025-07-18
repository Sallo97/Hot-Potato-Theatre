package org.example.backend.examples

import org.example.backend.game.Game
import org.example.backend.player.CoalitionalPlayer
import org.example.backend.player.Player
import org.example.backend.player.RationalPlayer
import org.example.backend.potato.Potato

/**
 * An example of a game composed of 5 rational players.
 */
val rationalExample by lazy {
    val potato = Potato(5, 5.0, 5.0)
    val set = mutableSetOf<RationalPlayer>().apply {
        for (i in 1..5) {
            val player = RationalPlayer(i)
            this.add(player)
        }
    }
    val game = Game(potato, set)
    GameExample(
        game,
        "Rational Game",
        "A simple game of 5 rational players with potato = $potato."
    )
}

/**
 * An example of a game composed of 5 gullible players.
 */
val gullibleExample by lazy {
    val potato = Potato(5, 5.0, 5.0)
    val set = mutableSetOf<CoalitionalPlayer>().apply {
        for (i in 1..5) {
            val player = CoalitionalPlayer(i, 0.5)
            this.add(player)
        }
    }

    val game = Game(potato, set)
    GameExample(
        game,
        "Gullible Game",
        "A simple game of 5 gullible players with potato = $potato."
    )
}

/**
 * An example of a game composed of 10 coalitional players.
 */
val coalitionalExample by lazy {
    val potato = Potato(lifetime = 10, gain = 5.0, loss = 10.0)
    val set = mutableSetOf<Player>().apply {
        for(i in 1..10) {
            val player = CoalitionalPlayer(i, 0.5)
            this.add(player)
        }
    }

    val game = Game(potato, set)
    GameExample(
        game,
        "Coalitional Game",
        "A simple game of 10 coalitional players with potato = $potato."
    )
}

/**
 * The set of all available examples.
 */
val examples:Set<GameExample> by lazy {
    val set = mutableSetOf<GameExample>().apply {
        this.add(rationalExample)
        this.add(gullibleExample)
        this.add(coalitionalExample)
    }
    set
}