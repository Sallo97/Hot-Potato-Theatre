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
    val message = "Input $name " + if (isNotNegative) "(must be strictly positive)" else ""

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
    val rangeText = range?.let { " (range: ${it.start}..${it.endInclusive})" } ?: ""
    val positiveText = if (isNotNegative) " (must be strictly positive)" else ""
    val message = "Input $name$positiveText$rangeText: "

    while (true) {
        print(message)
        val input = readln().toDoubleOrNull()

        if (input != null && (!isNotNegative || input >= 0) && (range?.contains(input) != false)) {
            return input.absoluteValue
        }

        println(invalidInputMsg)
    }
}

