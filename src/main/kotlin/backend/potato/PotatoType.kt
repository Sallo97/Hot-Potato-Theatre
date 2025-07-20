package backend.potato

import backend.player.PlayerType

/**
 * Describes the available types of potato
 */
enum class PotatoType(val displayName: String, val description: String) {
    FIXED("Fixed Potato", "Gain and loss remain fixed for all game runtime"),
    MUTABLE("Mutable Potato", "Gain and loss are updated at each turn");

    companion object {
        /**
         * Returns a string of all available types for potato.
         */
        fun getAvailableTypes(): String {
            return PotatoType.entries.toTypedArray().mapIndexed { index, type ->
                "${index + 1}. ${type.displayName} - ${type.description}"
            }.joinToString("\n")
        }

        /**
         * @return the PotatoType associated to [value], otherwise null.
         */
        fun fromInt(value: Int): PotatoType? {
            return entries.getOrNull(value - 1)
        }
    }
}