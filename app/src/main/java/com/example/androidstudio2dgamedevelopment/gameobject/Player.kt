package com.example.androidstudio2dgamedevelopment.gameobject

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.example.androidstudio2dgamedevelopment.GameDisplay
import com.example.androidstudio2dgamedevelopment.GameLoop
import com.example.androidstudio2dgamedevelopment.R
import com.example.androidstudio2dgamedevelopment.Utils
import com.example.androidstudio2dgamedevelopment.gamepanel.HealthBar
import com.example.androidstudio2dgamedevelopment.gamepanel.Joystick
import com.example.androidstudio2dgamedevelopment.graphics.Animator

/**
 * Player is the main character of the game, which the user can control with a touch joystick.
 * The player class is an extension of a Circle, which is an extension of a GameObject
 */
class Player(
    context: Context?,
    private val joystick: Joystick,
    positionX: Double,
    positionY: Double,
    radius: Double,
    animator: Animator) :
    Circle(context, ContextCompat.getColor(context!!, R.color.player ), positionX, positionY, radius) {

    private val healthBar: HealthBar
    private var healthPoints = MAX_HEALTH_POINTS
    private val animator: Animator
    val playerState: PlayerState

    init {
        healthBar = HealthBar(context, this)
        this.animator = animator
        playerState = PlayerState(this)
    }

    override fun update() {

        // Update velocity based on actuator of joystick
        velocityX = joystick.actuatorX * MAX_SPEED
        velocityY = joystick.actuatorY * MAX_SPEED

        // Update position
        positionX += velocityX
        positionY += velocityY

        // Update direction
        if (velocityX != 0.0 || velocityY != 0.0) {
            // Normalize velocity to get direction (unit vector of velocity)
            val distance = Utils.getDistanceBetweenPoints(0.0, 0.0, velocityX, velocityY)
            directionX = velocityX / distance
            directionY = velocityY / distance
        }
        playerState.update()
    }

    override fun draw(canvas: Canvas, gameDisplay: GameDisplay) {
        animator.draw(canvas, gameDisplay, this)
        healthBar.draw(canvas, gameDisplay)
    }

    // Only allow positive values
    var healthPoint: Int
        get() = healthPoints
        set(healthPoints) {
            // Only allow positive values
            if (healthPoints >= 0) this.healthPoints = healthPoints
        }

    companion object {
        const val SPEED_PIXELS_PER_SECOND = 400.0
        private val MAX_SPEED: Double = SPEED_PIXELS_PER_SECOND / GameLoop.Companion.MAX_UPS
        const val MAX_HEALTH_POINTS = 5
    }
}