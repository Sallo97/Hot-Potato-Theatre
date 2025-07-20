package backend.potato

import frontend.ui.absDoubleFromStdin
import frontend.ui.absIntFromStdin
import frontend.ui.absIntWithinRangeFromStdin

/**
 * @return a Potato object with parameters coming from user's input.
 */
fun createHotPotatoFromStdin() : Potato {
    println("Creating the hot potato...")
    val potatoType = potatoTypeFromStdin()
    val lifetime = absIntFromStdin("lifetime")
    val gain = absDoubleFromStdin("gain")
    val loss = absDoubleFromStdin("loss")
    val gainFactor = if(potatoType == PotatoType.FIXED) { 1.0 } else {
       absDoubleFromStdin("by how much the gain changes over time")
    }
    val lossFactor = if(potatoType == PotatoType.FIXED) { 1.0 } else {
        absDoubleFromStdin("by how much the loss changes over time")
    }
    val potato = Potato(lifetime, gain, loss, gainFactor, lossFactor)
    println("Created the potato: $potato")
    return potato
}

/**
 * Determines
 */
private fun potatoTypeFromStdin() : PotatoType {
    println("Select type of potato:")
    println(PotatoType.getAvailableTypes())
    val choice = absIntWithinRangeFromStdin("type of potato", 1..PotatoType.entries.size)
    val result = PotatoType.fromInt(choice)!!
    return result
}