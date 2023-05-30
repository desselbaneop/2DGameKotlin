package com.example.androidstudio2dgamedevelopment

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaPlayer
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Button
import com.example.androidstudio2dgamedevelopment.gameobject.Circle
import com.example.androidstudio2dgamedevelopment.gameobject.Circle.Companion.isColliding
import com.example.androidstudio2dgamedevelopment.gameobject.Enemy
import com.example.androidstudio2dgamedevelopment.gameobject.Enemy.Companion.readyToSpawn
import com.example.androidstudio2dgamedevelopment.gameobject.Player
import com.example.androidstudio2dgamedevelopment.gameobject.Spell
import com.example.androidstudio2dgamedevelopment.gamepanel.GameOver
import com.example.androidstudio2dgamedevelopment.gamepanel.Joystick
import com.example.androidstudio2dgamedevelopment.gamepanel.Performance
import com.example.androidstudio2dgamedevelopment.graphics.Animator
import com.example.androidstudio2dgamedevelopment.graphics.SpriteSheet
import com.example.androidstudio2dgamedevelopment.map.Tilemap

class Game(context: Context?) : SurfaceView(context), SurfaceHolder.Callback {
    private val tilemap: Tilemap
    private var joystickPointerId = 0
    private val joystick: Joystick
    private val player: Player
    private var gameLoop: GameLoop
    private val enemyList: MutableList<Enemy>
    private val spellList: MutableList<Spell>
    private var numberOfSpellsToCast = 0
    private val gameOver: GameOver
    private val performance: Performance
    private val gameDisplay: GameDisplay
    private var isGameOver = false
    private val restartButton: Button
    private var difficulty: Int
    private var kills: Int
    //TODO var mediaPlayer: MediaPlayer? = MediaPlayer.create(context, R.raw.sound)

    init {

        //TODO mediaPlayer?.start()

        // Get surface holder and add callback
        val surfaceHolder = holder
        surfaceHolder.addCallback(this)
        gameLoop = GameLoop(this, surfaceHolder)

        // Initialize game panels
        performance = Performance(context!!, gameLoop)
        gameOver = GameOver(context)
        joystick = Joystick(275, 700, 70, 40)

        // Initialize game objects
        val spriteSheet = SpriteSheet(context)
        val animator = Animator(spriteSheet.playerSpriteArray)
        player = Player(context, joystick, (2 * 500).toDouble(), 500.0, 32.0, animator)

        // Initialize display and center it around the player
        val displayMetrics = DisplayMetrics()
        (getContext() as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        gameDisplay = GameDisplay(displayMetrics.widthPixels, displayMetrics.heightPixels, player)
        restartButton = Button(context)
        restartButton.text = "Restart"
        restartButton.x = (gameDisplay.gameCenterX / 2f).toFloat()
        restartButton.y = (gameDisplay.gameCenterY / 2f).toFloat()

        // Initialize Tilemap
        tilemap = Tilemap(spriteSheet)
        isFocusable = true

        difficulty = 1

        kills = 0

        // Initialize enemy and spell lists
        enemyList = ArrayList()
        spellList = ArrayList()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Handle user input touch event actions
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                if (joystick.isPressed) {
                    // Joystick was pressed before this event -> cast spell
                    numberOfSpellsToCast++
                } else if (joystick.isPressed(event.x.toDouble(), event.y.toDouble())) {
                    // Joystick is pressed in this event -> setIsPressed(true) and store pointer id
                    joystickPointerId = event.getPointerId(event.actionIndex)
                    joystick.isPressed = true
                } else {
                    // Joystick was not previously pressed, and is not pressed in this event -> cast spell
                    numberOfSpellsToCast++
                }
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                if (joystick.isPressed) {
                    // Joystick was pressed previously and is now moved
                    joystick.setActuator(event.x.toDouble(), event.y.toDouble())
                }
                return true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                if (joystickPointerId == event.getPointerId(event.actionIndex)) {
                    // Joystick pointer was let go off -> setIsPressed(false) and resetActuator()
                    joystick.isPressed = false
                    joystick.resetActuator()
                }
                return true
            }
        }
        if (isGameOver) {
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    if (restartButton.isPressed) {
                        // Restart the game
                        restartGame()
                    }
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d("Game.java", "surfaceCreated()")
        if (gameLoop.state == Thread.State.TERMINATED) {
            val surfaceHolder = getHolder()
            surfaceHolder.addCallback(this)
            gameLoop = GameLoop(this, surfaceHolder)
        }
        gameLoop.startLoop()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d("Game.java", "surfaceChanged()")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d("Game.java", "surfaceDestroyed()")
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        // Draw Tilemap
        tilemap.draw(canvas, gameDisplay)

        // Draw game objects
        player.draw(canvas, gameDisplay)
        for (enemy in enemyList) {
            enemy.draw(canvas, gameDisplay)
        }
        for (spell in spellList) {
            spell.draw(canvas, gameDisplay)
        }
        if (isGameOver) {
            gameOver.draw(canvas)
            drawRestartButton(canvas)
        }

        // Draw game panels
        joystick.draw(canvas)
        performance.draw(canvas)

        // Draw Game over if the player is dead
        if (player.healthPoint <= 0) {
            gameOver.draw(canvas)
        }
    }

    private fun drawRestartButton(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.GREEN
        canvas.drawRect(
            restartButton.x,
            restartButton.y,
            restartButton.x + restartButton.width,
            restartButton.y + restartButton.height,
            paint
        )
        paint.color = Color.BLACK
        paint.textSize = 40f
        canvas.drawText(
            restartButton.text.toString(),
            restartButton.x + restartButton.width / 4,
            restartButton.y + restartButton.height / 2,
            paint
        )
    }

    fun update() {
        // Stop updating the game if the player is dead
        if (player.healthPoint <= 0) {
            isGameOver = true
            return
        }

        // Update game state
        joystick.update()
        player.update()

        // Spawn enemy
        if (readyToSpawn()) {
            val  enemy = Enemy(context, player)
            enemy.healthPoint = difficulty
            enemyList.add(enemy)
        }

        // Update states of all enemies
        for (enemy in enemyList) {
            enemy.update()
        }

        // Update states of all spells
        while (numberOfSpellsToCast > 0) {
            spellList.add(Spell(context, player))
            numberOfSpellsToCast--
        }
        for (spell in spellList) {
            spell.update()
        }

        // Iterate through enemyList and Check for collision between each enemy and the player and
        // spells in spellList.
        val iteratorEnemy = enemyList.iterator()
        while (iteratorEnemy.hasNext()) {
            val enemy: Circle = iteratorEnemy.next()
            if (isColliding(enemy, player)) {
                // Remove enemy if it collides with the player
                iteratorEnemy.remove()
                player.healthPoint = player.healthPoint - 1
                continue
            }
            val iteratorSpell = spellList.iterator()
            while (iteratorSpell.hasNext()) {
                val spell: Circle = iteratorSpell.next()
                // Remove enemy if it collides with a spell
                if (isColliding(spell, enemy)) {
                    //TODO
                    iteratorSpell.remove()
                    iteratorEnemy.remove()
                    break
                }
            }
        }

        // Update gameDisplay so that its center is set to the new center of the player's game coordinates
        gameDisplay.update()
    }

    private fun restartGame() {
        // Reset player's health to max value
        player.healthPoint = Player.MAX_HEALTH_POINTS

        // Reset game state
        isGameOver = false

        // Clear enemy and spell lists
        enemyList.clear()
        spellList.clear()
    }

    fun pause() {
        gameLoop.stopLoop()
    }
}