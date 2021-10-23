package chapter05

fun main() {
    val iterator = iterator {
        for (i in 0..4)
            yield(i * 4)
    }

    for (i in 0..5) {
        if (iterator.hasNext())
            println(iterator.next())
        else
            println("no element")
    }
}
