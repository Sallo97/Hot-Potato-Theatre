package org.example

import org.example.frontend.choiceFromStdin
import org.example.frontend.createGameFromStdin

fun main() {
    println("Welcome to Hot Potato Game!")
    while (true) {
        val game = createGameFromStdin()
        game.run()
        println(game.getEndGameInfo())

        val choice = choiceFromStdin("Wanna play another game?")
        if (choice == "n"  ) {
            break
        }
    }
    println("Exiting, Bye Bye!")
}









