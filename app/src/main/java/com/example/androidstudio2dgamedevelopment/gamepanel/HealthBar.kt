package com.example.androidstudio2dgamedevelopment.gamepanel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import com.example.androidstudio2dgamedevelopment.GameDisplay
import com.example.androidstudio2dgamedevelopment.R
import com.example.androidstudio2dgamedevelopment.gameobject.Player

/**
 * HealthBar display the players health to the screen
 */
class HealthBar(context: Context?, player: Player) {
    private val player: Player
    private val borderPaint: Paint
    private val healthPaint: Paint
    private val width: Int
    private val height: Int
    private val margin : Int
    // pixel value

    init {
        this.player = player
        width = 100
        height = 20
        margin = 2
        borderPaint = Paint()
        val borderColor: Int = ContextCompat.getColor(context!!, R.color.healthBarBorder)
        borderPaint.color = borderColor
        healthPaint = Paint()
        val healthColor: Int = ContextCompat.getColor(context, R.color.healthBarHealth)
        healthPaint.color = healthColor
    }

    fun draw(canvas: Canvas, gameDisplay: GameDisplay) {
        val x: Float = player.positionX.toFloat()
        val y: Float = player.positionY.toFloat()
        val distanceToPlayer = 30f
        val healthPointPercentage: Float =
            player.healthPoint.toFloat() / Player.MAX_HEALTH_POINTS

        // Draw border
        val borderLeft: Float
        val borderTop: Float
        val borderRight: Float
        val borderBottom: Float
        borderLeft = x - width / 2
        borderRight = x + width / 2
        borderBottom = y - distanceToPlayer
        borderTop = borderBottom - height
        canvas.drawRect(
            gameDisplay.gameToDisplayCoordinatesX(borderLeft.toDouble()).toFloat(),
            gameDisplay.gameToDisplayCoordinatesY(borderTop.toDouble()).toFloat(),
            gameDisplay.gameToDisplayCoordinatesX(borderRight.toDouble()).toFloat(),
            gameDisplay.gameToDisplayCoordinatesY(borderBottom.toDouble()).toFloat(),
            borderPaint
        )

        // Draw health
        val healthLeft: Float
        val healthTop: Float
        val healthRight: Float
        val healthBottom: Float
        val healthWidth: Float
        val healthHeight: Float
        healthWidth = (width - 2 * margin).toFloat()
        healthHeight = (height - 2 * margin).toFloat()
        healthLeft = borderLeft + margin
        healthRight = healthLeft + healthWidth * healthPointPercentage
        healthBottom = borderBottom - margin
        healthTop = healthBottom - healthHeight
        canvas.drawRect(
            gameDisplay.gameToDisplayCoordinatesX(healthLeft.toDouble()).toFloat(),
            gameDisplay.gameToDisplayCoordinatesY(healthTop.toDouble()).toFloat(),
            gameDisplay.gameToDisplayCoordinatesX(healthRight.toDouble()).toFloat(),
            gameDisplay.gameToDisplayCoordinatesY(healthBottom.toDouble()).toFloat(),
            healthPaint
        )
    }
}