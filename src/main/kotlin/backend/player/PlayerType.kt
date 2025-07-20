package backend.player

/**
 * Describes the available types of player
 *
 * @property [displayName] the name of that particular type of player.
 * @property [description] describes the behavior of that type of player.
 */
enum class PlayerType(val displayName: String, val description: String) {
    RATIONAL("Rational Player", "Never accepts the hot potato"),
    GULLIBLE("Gullible Player", "Always accepts the hot potato"),
    BARNUM("Barnum Player", "Is aware of the possibility of irrational actors among the population, thus it could consider taking the hot potato depending on them"),
    MYOPIC("Myopic Player", "Is not able to see the end to the game if its length surpasses its reasoning capability. If it sees the game as infinite, it will act rationally, otherwise will accept the good"),
    STOCHASTIC("Stochastic Player", "Interprets the decision of taking the hot potato as a static stochastic process, independent of other players"),
    DIRECT_ALTRUIST("Direct Altruist Player", "Concerned with the well-being of another player. For sake of simplicity we assume the beneficiary is always another player that did not get the hot potato previously"),
    BENTHAMITE("Benthamite Player", "Focused on maximizing the total payoff of the game, rather than its own. Still, if it knows that other players could take the risk of accepting the potato, it could deny the proposal"),
    COALITIONAL("Coalitional Player", "concerned in forming a coalition with other players of the same type in order to get a better payoff"),
    INPUT("Input Player", "the decision is provided by the user from Stdin");

    companion object {

        /**
         * Returns a string of all available types for player.
         */
        fun getAvailableTypes(): String {
            return entries.toTypedArray().mapIndexed { index, type ->
                "${index + 1}. ${type.displayName} - ${type.description}"
            }.joinToString("\n")
        }

        /**
         * @return the PlayerType associated to [value], otherwise null.
         */
        fun fromInt(value: Int): PlayerType? {
            return entries.getOrNull(value - 1)
        }
    }
}