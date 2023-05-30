package com.example.androidstudio2dgamedevelopment.gamepanel

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Joystick(
    private val outerCircleCenterPositionX: Int,
    private val outerCircleCenterPositionY: Int,
    outerCircleRadius: Int,
    innerCircleRadius: Int
) {
    private var innerCircleCenterPositionX: Int
    private var innerCircleCenterPositionY: Int
    private val outerCircleRadius: Int
    private val innerCircleRadius: Int
    private val innerCirclePaint: Paint
    private val outerCirclePaint: Paint
    var isPressed = false
    private var joystickCenterToTouchDistance = 0.0
    var actuatorX = 0.0
        private set
    var actuatorY = 0.0
        private set

    init {

        // Outer and inner circle make up the joystick
        innerCircleCenterPositionX = outerCircleCenterPositionX
        innerCircleCenterPositionY = outerCircleCenterPositionY

        // Radii of circles
        this.outerCircleRadius = outerCircleRadius
        this.innerCircleRadius = innerCircleRadius

        // paint of circles
        outerCirclePaint = Paint()
        outerCirclePaint.color = Color.GRAY
        outerCirclePaint.style = Paint.Style.FILL_AND_STROKE
        innerCirclePaint = Paint()
        innerCirclePaint.color = Color.BLUE
        innerCirclePaint.style = Paint.Style.FILL_AND_STROKE
    }

    fun draw(canvas: Canvas) {
        // Draw outer circle
        canvas.drawCircle(
            outerCircleCenterPositionX.toFloat(),
            outerCircleCenterPositionY.toFloat(),
            outerCircleRadius.toFloat(),
            outerCirclePaint
        )

        // Draw inner circle
        canvas.drawCircle(
            innerCircleCenterPositionX.toFloat(),
            innerCircleCenterPositionY.toFloat(),
            innerCircleRadius.toFloat(),
            innerCirclePaint
        )
    }

    fun update() {
        updateInnerCirclePosition()
    }

    private fun updateInnerCirclePosition() {
        innerCircleCenterPositionX =
            (outerCircleCenterPositionX + actuatorX * outerCircleRadius).toInt()
        innerCircleCenterPositionY =
            (outerCircleCenterPositionY + actuatorY * outerCircleRadius).toInt()
    }

    fun setActuator(touchPositionX: Double, touchPositionY: Double) {
        val deltaX = touchPositionX - outerCircleCenterPositionX
        val deltaY = touchPositionY - outerCircleCenterPositionY
        val deltaDistance = Math.sqrt(Math.pow(deltaX, 2.0) + Math.pow(deltaY, 2.0))
        if (deltaDistance < outerCircleRadius) {
            actuatorX = deltaX / outerCircleRadius
            actuatorY = deltaY / outerCircleRadius
        } else {
            actuatorX = deltaX / deltaDistance
            actuatorY = deltaY / deltaDistance
        }
    }

    fun isPressed(touchPositionX: Double, touchPositionY: Double): Boolean {
        joystickCenterToTouchDistance = Math.sqrt(
            Math.pow(outerCircleCenterPositionX - touchPositionX, 2.0) +
                    Math.pow(outerCircleCenterPositionY - touchPositionY, 2.0)
        )
        return joystickCenterToTouchDistance < outerCircleRadius
    }

    fun resetActuator() {
        actuatorX = 0.0
        actuatorY = 0.0
    }
}