package com.example.androidstudio2dgamedevelopment.gameobject

import android.graphics.Canvas
import com.example.androidstudio2dgamedevelopment.GameDisplay

/**
 * GameObject is an abstract class which is the foundation of all world objects in the game.
 */
abstract class GameObject {
    var positionX = 0.0
        protected set
    var positionY = 0.0
        protected set
    var velocityX = 0.0
    var velocityY = 0.0
    var directionX = 1.0
        protected set
    var directionY = 0.0
        protected set

    constructor() {}
    constructor(positionX: Double, positionY: Double) {
        this.positionX = positionX
        this.positionY = positionY
    }

    abstract fun draw(canvas: Canvas, gameDisplay: GameDisplay)
    abstract fun update()

    companion object {
        /**
         * getDistanceBetweenObjects returns the distance between two game objects
         * @param obj1
         * @param obj2
         * @return
         */
        fun getDistanceBetweenObjects(obj1: GameObject, obj2: GameObject): Double {
            return Math.sqrt(
                Math.pow(obj2.positionX - obj1.positionX, 2.0) +
                        Math.pow(obj2.positionY - obj1.positionY, 2.0)
            )
        }
    }
}