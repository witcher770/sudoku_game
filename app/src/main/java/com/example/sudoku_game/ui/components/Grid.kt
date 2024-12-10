package com.example.sudoku_game.ui.components

import SudokuSolver
import kotlin.random.Random

class Grid(private val n: Int = 3) {
    private val table = Array(n * n) { i ->
        IntArray(n * n) { j -> (i * n + i / n + j) % (n * n) + 1 }
    }

    init {
        println("The base table is ready!")
    }

    fun show() {
        table.forEach { row ->
            println(row.joinToString(" "))
        }
    }

    private fun transposing() {
        for (i in table.indices) {
            for (j in i + 1 until table[i].size) {
                val temp = table[i][j]
                table[i][j] = table[j][i]
                table[j][i] = temp
            }
        }
    }

    private fun swapRowsSmall() {
        val area = Random.nextInt(n)
        val line1 = Random.nextInt(n)
        var line2 = Random.nextInt(n)

        while (line1 == line2) {
            line2 = Random.nextInt(n)
        }

        val row1 = area * n + line1
        val row2 = area * n + line2
        table[row1] = table[row2].also { table[row2] = table[row1] }
    }

    private fun swapColumnsSmall() {
        transposing()
        swapRowsSmall()
        transposing()
    }

    private fun swapRowsArea() {
        val area1 = Random.nextInt(n)
        var area2 = Random.nextInt(n)

        while (area1 == area2) {
            area2 = Random.nextInt(n)
        }

        for (i in 0 until n) {
            val row1 = area1 * n + i
            val row2 = area2 * n + i
            table[row1] = table[row2].also { table[row2] = table[row1] }
        }
    }

    private fun swapColumnsArea() {
        transposing()
        swapRowsArea()
        transposing()
    }

    fun mix(amt: Int = 10) {
        val mixFunctions = listOf(
            { transposing() },
            { swapRowsSmall() },
            { swapColumnsSmall() },
            { swapRowsArea() },
            { swapColumnsArea() }
        )

        repeat(amt) {
            mixFunctions[Random.nextInt(mixFunctions.size)]()
        }
    }

    fun generatePuzzle(): Int {
        val flook = Array(n * n) { BooleanArray(n * n) }
        var iterator = 0
        var difficulty = n * n * n * n // Первоначально все элементы на месте

        while (iterator < n * n * n * n) {
            val i = Random.nextInt(n * n)
            val j = Random.nextInt(n * n)

            if (!flook[i][j]) { // Если её не смотрели
                iterator++
                flook[i][j] = true // Посмотрим

                val temp = table[i][j]  // Сохраним элемент на случай если без него нет решения или их слишком много
                table[i][j] = 0
                difficulty--  // Усложняем если убрали элемент

                val tableCopy = table.map { it.copyOf() }
                // Создаем экземпляр SudokuSolver

                val sudokuSolver = SudokuSolver()
                val solutionCount = sudokuSolver.countSolutions(n * n, n * n, tableCopy)


                if (solutionCount != 1) {
                    table[i][j] = temp
                    difficulty++
                }
            }
        }

        return difficulty
    }
}



//fun main() {
//    val example = Grid()
//    example.mix()
//
//    println("---------------------------")
//    example.show()
//
//    println("---------------------------")
//    val difficulty = example.generatePuzzle()
//    example.show()
//    println("Difficulty = $difficulty")
//}
