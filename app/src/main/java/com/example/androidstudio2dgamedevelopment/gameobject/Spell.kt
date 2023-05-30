package com.example.androidstudio2dgamedevelopment.gameobject

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.example.androidstudio2dgamedevelopment.GameDisplay
import com.example.androidstudio2dgamedevelopment.GameLoop
import com.example.androidstudio2dgamedevelopment.R

class Spell(context: Context?, spellcaster: Player) : Circle(
    context,
    ContextCompat.getColor(context!!, R.color.spell),
    spellcaster.positionX,
    spellcaster.positionY,
    25.0
) {
    init {
        velocityX = spellcaster.directionX * MAX_SPEED
        velocityY = spellcaster.directionY * MAX_SPEED
    }

    override fun draw(canvas: Canvas, gameDisplay: GameDisplay) {
        canvas.drawCircle(
            gameDisplay.gameToDisplayCoordinatesX(positionX).toFloat(),
            gameDisplay.gameToDisplayCoordinatesY(positionY).toFloat(),
            radius.toFloat(),
            paint
        )
    }

    override fun update() {
        positionX = positionX + velocityX
        positionY = positionY + velocityY
    }

    companion object {
        const val SPEED_PIXELS_PER_SECOND = 800.0
        private val MAX_SPEED: Double = SPEED_PIXELS_PER_SECOND / GameLoop.Companion.MAX_UPS
    }
}