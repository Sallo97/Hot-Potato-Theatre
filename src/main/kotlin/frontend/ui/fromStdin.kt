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
 * @return a correct input from stdin parsed as an absolute integer
 */
fun absIntFromStdin(name: String) : Int {
    var input: Int?
    while(true) {
        print("Input $name (will take the absolute value): ")
        input = readln().toIntOrNull()
        if(input != null) {
            return input.absoluteValue
        }
        println(invalidInputMsg)
    }
}

/**
 * @param [name] the name of the argument requested to Stdin.
 * @param [range] the range of integers in which the value must be inbetween (included)
 * @return a correct input from stdin parsed as an absolute integer.
 */
fun absIntWithinRangeFromStdin(name: String, range: IntRange) : Int {
    while(true){
        val input = absIntFromStdin(name)
        if(input in range) {
            return input
        } else {
            println(invalidInputMsg)
        }
    }
}
/**
 * @param [name] the name of the argument requested to Stdin.
 * @return a correct input from stdin parsed as an absolute double.
 */
fun absDoubleFromStdin(name:String) : Double {
    while(true) {
        print("Input $name (will take the absolute value): ")
        val input = readln().toDoubleOrNull()
        if(input != null) {
            return input.absoluteValue
        } else {
            println(invalidInputMsg)
        }
    }
}

/**
 * @param [name] the name of the argument requested to Stdin.
 * @param [range] the range of integers in which the value must be inbetween (included)
 * @return a correct input from stdin parsed as an absolute double
 */
fun absDoubleWithinRangeFromStdin(name: String, range: IntRange) : Double {
    var input: Double?
    while(true) {
        input = absDoubleFromStdin(name)
        if(input in range.start.toDouble()..range.endInclusive.toDouble()) {
            return input.absoluteValue
        }
        println(invalidInputMsg)
    }
}

