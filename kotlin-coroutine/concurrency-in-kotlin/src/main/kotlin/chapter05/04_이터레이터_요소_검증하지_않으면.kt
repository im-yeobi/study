package chapter05

fun main() {
    val iterator = iterator {
        yield(1)
    }
    println(iterator.next())
    println(iterator.next())    // NoSuchElementException 발생
}
