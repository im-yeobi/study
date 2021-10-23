package chapter05

fun main() {
    val fibonacci = iterator {
        yield(1)
        var current = 1
        var next = 1

        while (true) {
            yield(next)
            val tmpNext = current + next
            current = next
            next = tmpNext
        }
    }

    for (i in 0..10)
        print("${fibonacci.next()} ")
}
