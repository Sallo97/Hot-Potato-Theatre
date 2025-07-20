package frontend.ui

const val invalidInputMsg = "Invalid input, retrying..."

/**
 * Describes the restriction enforced on the requested input.
 */
enum class InputRestriction(val description: String) {
    ANY(""),
    NOT_NEGATIVE("(must be not negative)"),
    STRICTLY_POSITIVE("(must be strictly positive)");
}

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
 * @param [restriction] defines which property the input needs to satisfy.
 * @param [range] specifies the range in which the input should be.
 * @return a correct input from stdin parsed as an absolute integer
 */
fun intFromStdin(name: String,
                 restriction: InputRestriction = InputRestriction.ANY,
                 range: IntRange? = null): Int {
    val message = createInputMessage(name, restriction, range)

    while (true) {
        print(message)
        val input = readln().toIntOrNull()

        input?.let {
            val satisfy = satisfiesRestriction(it, restriction)
            if(satisfy) {
                return it
            }
        }

        println(invalidInputMsg)
    }
}

private fun <T> satisfiesRestriction (num: T, restriction: InputRestriction) : Boolean
        where T : Number, T : Comparable<T> {
    when (restriction) {
        InputRestriction.ANY -> {
            return true
        }
        InputRestriction.NOT_NEGATIVE -> {
            if(num >= 0 as T) {
                return true
            }
        }
        InputRestriction.STRICTLY_POSITIVE -> {
            if(num > 0 as T) {
                return true
            }
        }
    }
    return false
}

/**
 * @param [name] the name of the argument requested to Stdin.
 * @param [restriction] defines which property the input needs to satisfy.
 * @param [range] specifies the range in which the input should be.
 * @return a correct input from stdin parsed as an absolute double.
 */
fun doubleFromStdin(
    name: String,
    restriction: InputRestriction = InputRestriction.ANY,
    range: ClosedFloatingPointRange<Double>? = null
): Double {
    val message = createInputMessage(name, restriction, range)

    while (true) {
        print(message)
        val input = readln().toDoubleOrNull()
        input?.let {
            val satisfy = satisfiesRestriction(it, restriction)
            if(satisfy) {
                return it
            }
        }

        println(invalidInputMsg)
    }
}

private fun <T : Comparable<T>> createInputMessage(name: String,
                                                   restriction: InputRestriction = InputRestriction.ANY,
                                                   range: ClosedRange<T>? = null) : String {
    val startMessage = "Input $name"
    val rangeText = range?.let { "(range: ${it.start}..${it.endInclusive})" } ?: ""

    return "$startMessage ${restriction.description} $rangeText: "
}