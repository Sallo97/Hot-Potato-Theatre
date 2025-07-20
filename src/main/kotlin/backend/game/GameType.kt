package backend.game

enum class GameType(val displayName: String, val description: String) {
    HOMOGENEOUS("Homogenous Game", "game where players are all of the same type"),
    MIXED("Mixed Game", "game where players can be of different types");

    companion object {
        /**
         * Returns a string of all available types for game.
         */
        fun getAvailableTypes(): String {
            return GameType.entries.toTypedArray().mapIndexed { index, type ->
                "${index + 1}. ${type.displayName} - ${type.description}"
            }.joinToString("\n")
        }

        /**
         * @return the GameType associated to [value], otherwise null.
         */
        fun fromInt(value: Int): GameType? {
            return GameType.entries.getOrNull(value - 1)
        }
    }
}