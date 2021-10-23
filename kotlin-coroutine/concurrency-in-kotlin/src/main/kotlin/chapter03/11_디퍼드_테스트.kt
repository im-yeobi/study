package chapter03

fun main() {
    val origin = listOf(1, 2, 3)
    val dest: MutableList<Int> = mutableListOf()
    origin.mapTo(dest) {
        it
    }

    println(dest)
}
