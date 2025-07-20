package backend.potato

import frontend.ui.InputRestriction
import frontend.ui.doubleFromStdin
import frontend.ui.intFromStdin

/**
 * @return a Potato object with parameters coming from user's input.
 */
fun createHotPotatoFromStdin() : Potato {
    println("Creating the hot potato...")
    val potatoType = potatoTypeFromStdin()
    val lifetime = intFromStdin("lifetime", InputRestriction.STRICTLY_POSITIVE)
    val baseGain = doubleFromStdin("gain", InputRestriction.NOT_NEGATIVE)
    val baseLoss = doubleFromStdin("loss", InputRestriction.NOT_NEGATIVE)
    val gainFactor = if(potatoType == PotatoType.FIXED) { 1.0 } else {
       doubleFromStdin("by how much the gain changes over time")
    }
    val lossFactor = if(potatoType == PotatoType.FIXED) { 1.0 } else {
        doubleFromStdin("by how much the loss changes over time")
    }

    val potato = Potato(lifetime, baseGain, baseLoss, potatoType, gainFactor, lossFactor)
    println("Created the potato: $potato")
    return potato
}

/**
 * Determines
 */
private fun potatoTypeFromStdin() : PotatoType {
    println("Select type of potato:")
    println(PotatoType.getAvailableTypes())
    val choice = intFromStdin("type of potato", InputRestriction.STRICTLY_POSITIVE, 1..PotatoType.entries.size)
    val result = PotatoType.fromInt(choice)!!
    return result
}