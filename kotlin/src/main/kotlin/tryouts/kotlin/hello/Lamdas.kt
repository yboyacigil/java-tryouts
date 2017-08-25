package tryouts.kotlin.hello

fun execBody(msg: String, body: () -> Unit) {
    println("-- $msg")
    body()
}

fun main(args: Array<String>) {

    execBody("Filter out empty strings from list and map to upper case") {
        val strings = listOf<String>("a", "b", "c", "d", "");
        println("$strings -> ${strings.filter(String::isNotEmpty).map(String::toUpperCase)}")
    }

    execBody("Access values of a map ignoring key") {
        val map = mapOf<Int, String>(0 to "Zero", 1 to "One", 2 to "Two")
        println("Will print values of $map")
        map.forEach { _, name -> println(name) }
    }

    execBody("Create a sum function using lambda expressions ") {
        val sum = { x: Int, y: Int -> x + y }
        println("sum of 2 and 3 is ${sum(2, 3)}")
    }

}

