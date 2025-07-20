package frontend.ui

import kotlin.math.absoluteValue

const val invalidInputMsg = "Invalid input, retrying..."


/**
 * @param [question] the question asked the user for which it needs to respond.
 * @return true if the user choose "y", false if it chooses "n".
 */
fun choiceFromStdin(question: String) : Boolean {
    while(true) {
        print("$question Press Y/y for Yes, N/n for No: ")
        val choice = readln().lowercase()
        if (choice == "n" || choice == "y") {
            return choice == "y"
        }
        println(invalidInputMsg)
    }
}

/**
 * @param [name] the name of the argument requested to Stdin.
 * @param [isNotNegative] require the input to be >= 0.
 * @param [range] specifies the range in which the input should be.
 * @return a correct input from stdin parsed as an absolute integer
 */
fun intFromStdin(name: String, isNotNegative: Boolean = false, range: IntRange? = null): Int {
    val message = createInputMessage(name, isNotNegative, range)

    while (true) {
        print(message)
        val input = readln().toIntOrNull()

        if (input != null && (!isNotNegative || input >= 0) && (range?.contains(input) != false)) {
            return input.absoluteValue
        }
        println(invalidInputMsg)
    }
}

/**
 * @param [name] the name of the argument requested to Stdin.
 * @param [isNotNegative] require the input to be >=0.
 * @param [range] specifies the range in which the input should be.
 * @return a correct input from stdin parsed as an absolute double.
 */
fun doubleFromStdin(
    name: String,
    isNotNegative: Boolean = false,
    range: ClosedFloatingPointRange<Double>? = null
): Double {
    val message = createInputMessage(name, isNotNegative, range)

    while (true) {
        print(message)
        val input = readln().toDoubleOrNull()

        if (input != null && (!isNotNegative || input >= 0) && (range?.contains(input) != false)) {
            return input.absoluteValue
        }

        println(invalidInputMsg)
    }
}

private fun <T : Comparable<T>> createInputMessage(name: String, isNotNegative: Boolean = false, range: ClosedRange<T>? = null) : String {
    val startMessage = "Input $name"
    val positiveMessage = if (isNotNegative) {
        " (must be strictly positive)"
    } else {
        ""
    }
    val rangeText = range?.let { " (range: ${it.start}..${it.endInclusive})" } ?: ""

    return "$startMessage$positiveMessage$rangeText: "
}