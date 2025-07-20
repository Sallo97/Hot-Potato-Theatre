package backend.game

import backend.player.createSetOfPlayersFromStdin
import backend.potato.createHotPotatoFromStdin
import frontend.ui.intFromStdin

/**
 * @return a Game Object with parameters coming from user's input
 */
fun createGameFromStdin() : Game {
    println("Creating game...")
    val potato = createHotPotatoFromStdin()
    val gameType = gameTypeFromStdin()

    val numOfPlayers = intFromStdin("number of players", true)
    val set = createSetOfPlayersFromStdin(numOfPlayers, gameType)

    val game = Game(potato, set)
    return game
}

/**
 * @return true if the user asked for a Homogenous game, false otherwise.
 */
private fun gameTypeFromStdin() : GameType {
    println("Select type of game:")
    println(GameType.getAvailableTypes())

    val choice = intFromStdin("type of game", true, 1..GameType.entries.size)
    val result = GameType.fromInt(choice)
    return result!!
}