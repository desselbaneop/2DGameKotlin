package com.example.androidstudio2dgamedevelopment.gamepanel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import com.example.androidstudio2dgamedevelopment.GameLoop
import com.example.androidstudio2dgamedevelopment.R

class Performance(private val context: Context, gameLoop: GameLoop) {
    private val gameLoop: GameLoop

    init {
        this.gameLoop = gameLoop
    }

    fun draw(canvas: Canvas) {
        drawUPS(canvas)
        drawFPS(canvas)
    }

    fun drawUPS(canvas: Canvas) {
        val averageUPS = java.lang.Double.toString(gameLoop.averageUPS)
        val paint = Paint()
        val color: Int = ContextCompat.getColor(context, R.color.magenta)
        paint.color = color
        paint.textSize = 50f
        canvas.drawText("UPS: $averageUPS", 100f, 100f, paint)
    }

    fun drawFPS(canvas: Canvas) {
        val averageFPS = java.lang.Double.toString(gameLoop.averageFPS)
        val paint = Paint()
        val color: Int = ContextCompat.getColor(context, R.color.magenta)
        paint.color = color
        paint.textSize = 50f
        canvas.drawText("FPS: $averageFPS", 100f, 200f, paint)
    }
}