package com.example.androidstudio2dgamedevelopment.gameobject

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.example.androidstudio2dgamedevelopment.GameDisplay
import com.example.androidstudio2dgamedevelopment.GameLoop
import com.example.androidstudio2dgamedevelopment.R

/**
 * Enemy is a character which always moves in the direction of the player.
 * The Enemy class is an extension of a Circle, which is an extension of a GameObject
 */
class Enemy : Circle {
    private var player: Player
    private var healthPoints = 1

    constructor(
        context: Context?,
        player: Player,
        positionX: Double,
        positionY: Double,
        radius: Double,
        hp: Int
    ) : super(
        context,
        ContextCompat.getColor(context!!, R.color.enemy),
        positionX,
        positionY,
        radius
    ) {
        this.player = player
    }

    var healthPoint: Int
        get() = healthPoints
        set(healthPoints) {
            // Only allow positive values
            if (healthPoints >= 0) this.healthPoints = healthPoints
        }

    /**
     * Enemy is an overload constructor used for spawning enemies in random locations
     * @param context
     * @param player
     */
    constructor(context: Context?, player: Player) : super(
        context,
        ContextCompat.getColor(context!!, R.color.enemy),
        Math.random() * 1000,
        Math.random() * 1000,
        30.0
    ) {
        this.player = player
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
        // =========================================================================================
        //   Update velocity of the enemy so that the velocity is in the direction of the player
        // =========================================================================================
        // Calculate vector from enemy to player (in x and y)
        val distanceToPlayerX = player.positionX - positionX
        val distanceToPlayerY = player.positionY - positionY

        // Calculate (absolute) distance between enemy (this) and player
        val distanceToPlayer = getDistanceBetweenObjects(this, player)

        // Calculate direction from enemy to player
        val directionX = distanceToPlayerX / distanceToPlayer
        val directionY = distanceToPlayerY / distanceToPlayer

        // Set velocity in the direction to the player
        if (distanceToPlayer > 0) { // Avoid division by zero
            velocityX = directionX * MAX_SPEED
            velocityY = directionY * MAX_SPEED
        } else {
            velocityX = 0.0
            velocityY = 0.0
        }

        // =========================================================================================
        //   Update position of the enemy
        // =========================================================================================
        positionX += velocityX
        positionY += velocityY
    }

    companion object {
        private const val SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND * 0.6
        private val MAX_SPEED: Double = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS
        private const val SPAWNS_PER_MINUTE = 20.0
        private const val SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE / 60.0
        private val UPDATES_PER_SPAWN: Double = GameLoop.MAX_UPS / SPAWNS_PER_SECOND
        private var updatesUntilNextSpawn = UPDATES_PER_SPAWN

        /**
         * readyToSpawn checks if a new enemy should spawn, according to the decided number of spawns
         * per minute (see SPAWNS_PER_MINUTE at top)
         * @return
         */
        @JvmStatic
        fun readyToSpawn(): Boolean {
            return if (updatesUntilNextSpawn <= 0) {
                updatesUntilNextSpawn += UPDATES_PER_SPAWN
                true
            } else {
                updatesUntilNextSpawn--
                false
            }
        }
    }
}