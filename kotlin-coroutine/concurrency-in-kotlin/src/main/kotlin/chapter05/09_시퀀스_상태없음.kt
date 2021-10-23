package chapter05

fun main() {
    val sequence = sequence {
        println("yielding 1")
        yield(1)
        println("yielding 2")
        yield(2)
        println("yielding 3")
        yield(3)
//        for (i in 0..9) {
//            println("yielding $i")
//            yield(i)
//        }
    }

    println("requesting index 1")
    sequence.elementAt(1)

    println("requesting index 2")
    sequence.elementAt(2)
}
