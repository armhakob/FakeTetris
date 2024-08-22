package com.example.tetris
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import kotlin.math.min
import kotlin.random.Random

class TetrisView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val rows = 20
    private val cols = 10
    private val blockSize = 80
    private val board = Array(rows) { IntArray(cols) }

    private var currentPiece: Tetris = Tetris.random()
    private var pieceX = 3
    private var pieceY = 0

    private val paint = Paint()

    init {
        clearBoard()
    }

    private fun clearBoard() {
        for (y in board.indices) {
            board[y] = IntArray(cols)
        }
    }

    private fun movePiece(dx: Int, dy: Int) {
        if (canMove(dx, dy)) {
            pieceX += dx
            pieceY += dy
            invalidate()
        } else if (dy > 0) {
            lockPiece()
            clearLines()
            spawnNewPiece()
        }
    }

    private fun canMove(dx: Int, dy: Int): Boolean {
        for (y in currentPiece.shape.indices) {
            for (x in currentPiece.shape[y].indices) {
                if (currentPiece.shape[y][x] != 0) {
                    val newX = pieceX + x + dx
                    val newY = pieceY + y + dy
                    if (newX < 0 || newX >= cols || newY >= rows || (newY >= 0 && board[newY][newX] != 0)) {
                        return false
                    }
                }
            }
        }
        return true
    }

    private fun lockPiece() {
        for (y in currentPiece.shape.indices) {
            for (x in currentPiece.shape[y].indices) {
                if (currentPiece.shape[y][x] != 0) {
                    board[pieceY + y][pieceX + x] = currentPiece.color
                }
            }
        }
    }

    private fun clearLines() {
        for (y in board.indices) {
            if (board[y].all { it != 0 }) {
                for (row in y downTo 1) {
                    board[row] = board[row - 1]
                }
                board[0] = IntArray(cols)
            }
        }
    }

    private fun spawnNewPiece() {
        currentPiece = Tetris.random()
        pieceX = 3
        pieceY = 0
        if (!canMove(0, 0)) {
            clearBoard()
        }
    }

    private var initialX = 0f
    private var initialY = 0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = event.x
                initialY = event.y
            }
            MotionEvent.ACTION_UP -> {
                val deltaX = event.x - initialX
                val deltaY = event.y - initialY

                if (abs(deltaX) > abs(deltaY)) {
                    if (deltaX > 0) {
                        movePiece(1, 0)
                    } else {
                        movePiece(-1, 0)
                    }
                } else if (deltaY > 0 && abs(deltaY) > blockSize / 2) {
                    movePiece(0, 1)
                } else {
                    rotatePiece()
                }
                invalidate()
            }
        }
        return true
    }

    private fun rotatePiece() {
        val rotated = currentPiece.rotate()
        if (canMove(0, 0)) {
            currentPiece = rotated
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the board
        for (y in board.indices) {
            for (x in board[y].indices) {
                if (board[y][x] != 0) {
                    paint.color = board[y][x]
                    canvas.drawRect(
                        (x * blockSize).toFloat(), (y * blockSize).toFloat(),
                        ((x + 1) * blockSize).toFloat(), ((y + 1) * blockSize).toFloat(), paint
                    )
                }
            }
        }

        for (y in currentPiece.shape.indices) {
            for (x in currentPiece.shape[y].indices) {
                if (currentPiece.shape[y][x] != 0) {
                    paint.color = currentPiece.color
                    canvas.drawRect(
                        ((pieceX + x) * blockSize).toFloat(), ((pieceY + y) * blockSize).toFloat(),
                        ((pieceX + x + 1) * blockSize).toFloat(), ((pieceY + y + 1) * blockSize).toFloat(), paint
                    )
                }
            }
        }

        paint.color = Color.BLACK
        for (y in 0..rows) {
            canvas.drawLine(0f, (y * blockSize).toFloat(), (cols * blockSize).toFloat(), (y * blockSize).toFloat(), paint)
        }
        for (x in 0..cols) {
            canvas.drawLine((x * blockSize).toFloat(), 0f, (x * blockSize).toFloat(), (rows * blockSize).toFloat(), paint)
        }
    }
}
