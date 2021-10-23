package chapter05

fun main() {
    val sequence = sequence {
        yield(1)
        yield("A")
        yield(10L)
    }
}
