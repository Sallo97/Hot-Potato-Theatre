package backend.examples

import backend.game.Game
import backend.game.GameType
import backend.player.CoalitionalPlayer
import backend.player.GulliblePlayer
import backend.player.Player
import backend.player.RationalPlayer
import backend.potato.Potato

/**
 * An example of a game composed of 5 rational players.
 */
val rationalExample by lazy {
    val potato = Potato(200, 1000.0, 5.0)
    val numOfPlayers = 100
    val set = mutableSetOf<RationalPlayer>().apply {
        for (i in 1..numOfPlayers) {
            val player = RationalPlayer(i)
            this.add(player)
        }
    }
    val game = Game(potato, set, type = GameType.HOMOGENEOUS)
    GameExample(
        game,
        "Rational Game",
        "A simple game of $numOfPlayers rational players with potato = $potato."
    )
}

/**
 * An example of a game composed of 5 gullible players.
 */
val gullibleExample by lazy {
    val potato = Potato(200, 5.0, 1000.0)
    val numOfPlayer = 100
    val set = mutableSetOf<GulliblePlayer>().apply {
        for (i in 1..numOfPlayer) {
            val player = GulliblePlayer(i)
            this.add(player)
        }
    }

    val game = Game(potato, set, GameType.HOMOGENEOUS)
    GameExample(
        game,
        "Gullible Game",
        "A simple game of $numOfPlayer gullible players with potato = $potato."
    )
}

/**
 * An example of a game composed of 10 coalitional players.
 */
val coalitionalExample by lazy {
    val potato = Potato(lifetime = 10, baseGain = 5.0, baseLoss = 10.0)
    val set = mutableSetOf<Player>().apply {
        for(i in 1..10) {
            val player = CoalitionalPlayer(i)
            this.add(player)
        }
    }

    val game = Game(potato, set, GameType.HOMOGENEOUS)
    GameExample(
        game,
        "Coalitional Game",
        "A simple game of 10 coalitional players with potato = $potato."
    )
}

/**
 * The set of all available examples.
 */
val examples:List<GameExample> by lazy {
    val list = mutableListOf<GameExample>().apply {
        this.add(rationalExample)
        this.add(gullibleExample)
        this.add(coalitionalExample)
    }
    list
}