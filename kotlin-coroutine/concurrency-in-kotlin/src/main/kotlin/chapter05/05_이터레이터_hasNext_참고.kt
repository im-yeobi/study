package chapter05

fun main() {
    val iterator = iterator {
        println("yielding 1")
        yield(1)
        println("yielding 2")
        yield(2)
    }

    iterator.next()

    if (iterator.hasNext()) {
        println("iterator has next")
        iterator.next()
    }
}
