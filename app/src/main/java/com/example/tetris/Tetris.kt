package com.example.tetris

import kotlin.random.Random

class Tetris(val shape: Array<IntArray>, val color: Int) {

    fun rotate(): Tetris {
        val rotatedShape = Array(shape[0].size) { IntArray(shape.size) }
        for (y in shape.indices) {
            for (x in shape[y].indices) {
                rotatedShape[x][shape.size - y - 1] = shape[y][x]
            }
        }
        return Tetris(rotatedShape, color)
    }

    companion object {
        private val shapes = listOf(
            Tetris(arrayOf(intArrayOf(1, 1, 1, 1)), 0xFF00FFFF.toInt()), // I
            Tetris(arrayOf(intArrayOf(1, 1), intArrayOf(1, 1)), 0xFFFFFF00.toInt()), // O
            Tetris(arrayOf(intArrayOf(0, 1, 0), intArrayOf(1, 1, 1)), 0xFFFF00FF.toInt()), // T
            Tetris(arrayOf(intArrayOf(1, 0, 0), intArrayOf(1, 1, 1)), 0xFFFFA500.toInt()), // L
            Tetris(arrayOf(intArrayOf(0, 0, 1), intArrayOf(1, 1, 1)), 0xFF0000FF.toInt()), // J
            Tetris(arrayOf(intArrayOf(1, 1, 0), intArrayOf(0, 1, 1)), 0xFF00FF00.toInt()), // S
            Tetris(arrayOf(intArrayOf(0, 1, 1), intArrayOf(1, 1, 0)), 0xFFFF0000.toInt())  // Z
        )

        fun random(): Tetris = shapes[Random.nextInt(shapes.size)]
    }
}
