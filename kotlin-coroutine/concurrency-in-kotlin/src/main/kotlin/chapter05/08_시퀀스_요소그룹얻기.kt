package chapter05

fun main() {
    val sequence = sequence {
        yield(1)
        yield(10L)
        yield("hello")
    }

    val take = sequence.take(5)
    println(take.joinToString())
}
