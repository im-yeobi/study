package chapter05

fun main() {
    val sequence = sequence {
        yield(1)
        yield("hello")
    }

    println(sequence.elementAt(1))
    sequence.elementAtOrElse(2) { it * 1 }
}
