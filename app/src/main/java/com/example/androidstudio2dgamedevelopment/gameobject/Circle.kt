package com.example.androidstudio2dgamedevelopment.gameobject

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.example.androidstudio2dgamedevelopment.GameDisplay

/**
 * Circle is an abstract class which implements a draw method from GameObject for drawing the object
 * as a circle.
 */
abstract class Circle(
    context: Context?,
    color: Int,
    positionX: Double,
    positionY: Double,
    var radius: Double
) : GameObject(positionX, positionY) {
    var paint: Paint = Paint()

    init {

        // Set colors of circle
        paint.color = color
    }

    override fun draw(canvas: Canvas, gameDisplay: GameDisplay) {
        canvas.drawCircle(
            gameDisplay.gameToDisplayCoordinatesX(positionX).toFloat(),
            gameDisplay.gameToDisplayCoordinatesY(positionY).toFloat(),
            radius.toFloat(),
            paint
        )
    }

    companion object {
        /**
         * isColliding checks if two circle objects are colliding, based on their positions and radii.
         * @param obj1
         * @param obj2
         * @return
         */
        @JvmStatic
        fun isColliding(obj1: Circle, obj2: Circle): Boolean {
            val distance = getDistanceBetweenObjects(obj1, obj2)
            val distanceToCollision = obj1.radius + obj2.radius
            return if (distance < distanceToCollision) true else false
        }
    }
}