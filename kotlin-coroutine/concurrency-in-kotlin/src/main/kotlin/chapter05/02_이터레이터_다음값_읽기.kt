package chapter05

fun main() {
    val iterator: Iterator<Any> = iterator {
        yield(1)
        yield(10L)
        yield("Hello")
    }

    println(iterator.next())
    println(iterator.next())
    println(iterator.next())
}
