package chapter05

fun main() {
    val fibonacci = sequence {
        yield(1)
        var current = 1
        var next = 1

        while(true) {
            yield(next)
            val tmpNext = current + next
            current = next
            next = tmpNext
        }
    }

    val take = fibonacci.take(10)
    println(fibonacci.elementAt(2))
    println(take.joinToString())
}
