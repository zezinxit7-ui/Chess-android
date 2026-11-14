package com.chessandroid.core.chess

import com.github.bhlangonijr.chesslib.Board

class ChessGame {

    private var board = Board()
    private val previousPositions = mutableListOf<String>()
    private val playedMoves = mutableListOf<String>()

    val fen: String
        get() = board.fen

    val moveCount: Int
        get() = playedMoves.size

    val moves: List<String>
        get() = playedMoves.toList()

    val isWhiteTurn: Boolean
        get() = fen.split(" ").getOrNull(1) == "w"

    val legalMoves: List<String>
        get() = board.legalMoves().map { it.toString().lowercase() }

    fun pieceAt(square: String): Char? {
        if (square.length != 2) return null

        val file = square[0] - 'a'
        val rank = square[1].digitToIntOrNull() ?: return null

        if (file !in 0..7 || rank !in 1..8) return null

        val placement = fen.substringBefore(" ")
        val rows = placement.split("/")
        var currentFile = 0

        for (rowIndex in rows.indices) {
            currentFile = 0

            for (symbol in rows[rowIndex]) {
                if (symbol.isDigit()) {
                    currentFile += symbol.digitToInt()
                } else {
                    if (rowIndex == 8 - rank && currentFile == file) {
                        return symbol
                    }
                    currentFile++
                }
            }
        }

        return null
    }

    fun play(uci: String): Boolean {
        val normalized = uci.lowercase()

        val legal = board.legalMoves().firstOrNull {
            it.toString().equals(normalized, ignoreCase = true)
        } ?: return false

        previousPositions.add(board.fen)
        board.doMove(legal)
        playedMoves.add(normalized)

        return true
    }

    fun undo(): Boolean {
        if (previousPositions.isEmpty()) return false

        val previousFen = previousPositions.removeAt(previousPositions.lastIndex)
        if (playedMoves.isNotEmpty()) {
            playedMoves.removeAt(playedMoves.lastIndex)
        }

        board.loadFromFen(previousFen)
        return true
    }

    fun restart() {
        board = Board()
        previousPositions.clear()
        playedMoves.clear()
    }

    fun loadPosition(positionFen: String): Boolean {
        return try {
            val testBoard = Board()
            testBoard.loadFromFen(positionFen)

            board = testBoard
            previousPositions.clear()
            playedMoves.clear()
            true
        } catch (_: Exception) {
            false
        }
    }

    fun isCheckmate(): Boolean {
        return board.isMated
    }

    fun isStalemate(): Boolean {
        return board.isStaleMate
    }
}
