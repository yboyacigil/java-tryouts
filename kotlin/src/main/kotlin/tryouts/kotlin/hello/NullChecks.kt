package tryouts.kotlin.hello

fun parseInt(s: String): Int? {
    try {
        return s.toInt()
    } catch (e: NumberFormatException) {
        println("ERROR: ${s} Cannot parse to int")
    }
    return null;
}

fun methodOne(args: Array<String>) {
    val x = parseInt(args[0])
    val y = parseInt(args[1])

    // x * y not compiles if not checked against null
    if (x != null && y != null) {
        println(x * y);
    } else {
        println("One of two arguments is null")
    }
}

fun methodTwo(args: Array<String>) {
    // Default to one using elvis operator
    val x = parseInt(args[0]) ?: 1
    val y = parseInt(args[1]) ?: 1

    println(x * y)
}

fun main(args: Array<String>) {
    if (args.size < 2) {
        println("Must provide at least 2 args")
    } else {
        println("-- method 1")
        methodOne(args)
        println("-- method 2")
        methodTwo(args)
    }
}


