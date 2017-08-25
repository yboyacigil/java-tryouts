package tryouts.kotlin.hello

fun main(args: Array<String>) {
    val name = if (args.isEmpty()) "World" else args[0]
    println("Hello, $name!")
}


