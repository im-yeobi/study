package chapter05

fun main() {
    val iterator: Iterator<Any> = iterator {
        yield(1)
        yield(10L)
        yield("Hello")
    }

    /*
    val errorIterator: Iterator<String> = iterator {
        yield(1)
        yield(10L)
        yield("Hello")
    }
     */
}
