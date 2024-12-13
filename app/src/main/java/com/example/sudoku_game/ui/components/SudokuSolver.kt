class SudokuSolver {
    private val solutions = mutableListOf<List<MutableList<Int>>>()
    fun isSellCorrect(board: List<IntArray>, row: Int, col: Int, num: Int): Boolean {
        // Проверка строки
        for (c in board[row].indices) {
            if (board[row][c] == num) return false
        }
        // Проверка столбца
        for (r in board.indices) {
            if (board[r][col] == num) return false
        }
        // Проверка квадрата
        val sqrt = Math.sqrt(board.size.toDouble()).toInt()
        val startRow = row / sqrt * sqrt
        val startCol = col / sqrt * sqrt
        for (r in startRow until startRow + sqrt) {
            for (c in startCol until startCol + sqrt) {
                if (board[r][c] == num) return false
            }
        }
        return true
    }

    // Основная функция для подсчёта решений
    fun countSolutions(rows: Int, cols: Int, board: List<IntArray>): Int {
        solutions.clear() // Очищаем список решений перед началом
        var solutionCount = 0



        fun solve(row: Int, col: Int): Boolean {
            if (row == rows) {
                // Если всё заполнено, сохраняем решение
                solutionCount++
                solutions.add(board.map { it.toMutableList() }) // Копируем текущую доску
                return false // Не останавливаемся, чтобы найти все решения
            }

            val nextRow = if (col == cols - 1) row + 1 else row
            val nextCol = if (col == cols - 1) 0 else col + 1

            if (board[row][col] != 0) {
                return solve(nextRow, nextCol)
            }

            for (num in 1..rows) {
                if (isSellCorrect(board, row, col, num)) {
                    board[row][col] = num
                    if (solve(nextRow, nextCol)) return true
                    board[row][col] = 0 // Откат
                }
            }

            return false
        }

        solve(0, 0)
        return solutionCount
    }

    // Метод для получения списка решений
    fun getSolutions(): List<List<MutableList<Int>>> = solutions

    fun show() {
        solutions.forEach { matrix ->
            matrix.forEach { row ->
                println(row.joinToString(" "))
            }
            println("------------------")
        }
    }
}

//fun main() {
//    val sudoku = listOf(
//        intArrayOf(3, 9, 4, 0, 7, 0, 0, 1, 0),
//        intArrayOf(6, 7, 2, 1, 9, 5, 0, 0, 0),
//        intArrayOf(8, 1, 5, 0, 0, 0, 0, 6, 0),
//        intArrayOf(7, 5, 0, 0, 6, 0, 0, 0, 3),
//        intArrayOf(0, 2, 0, 8, 0, 3, 0, 0, 1),
//        intArrayOf(0, 0, 1, 0, 2, 0, 0, 0, 6),
//        intArrayOf(0, 0, 0, 0, 0, 0, 2, 8, 0),
//        intArrayOf(2, 0, 0, 4, 1, 9, 0, 0, 5),
//        intArrayOf(0, 4, 0, 0, 8, 0, 0, 7, 9)
//    )
//
//    val solver = SudokuSolver()
//    val solutions = solver.countSolutions(9, 9, sudoku)
//    solver.show()
//    println("Количество возможных решений: $solutions")
//}
