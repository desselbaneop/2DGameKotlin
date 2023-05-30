package com.example.androidstudio2dgamedevelopment

import android.app.Activity
import android.os.Bundle
import android.util.Log

/**
 * MainActivity is the entry point to our application.
 */
class MainActivity : Activity() {
    private var game: Game? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity.java", "onCreate()")
        super.onCreate(savedInstanceState)

        // Set content view to game, so that objects in the Game class can be rendered to the screen
        game = Game(this)
        setContentView(game)
    }

    override fun onStart() {
        Log.d("MainActivity.java", "onStart()")
        super.onStart()
    }

    override fun onResume() {
        Log.d("MainActivity.java", "onResume()")
        super.onResume()
    }

    override fun onPause() {
        Log.d("MainActivity.java", "onPause()")
        game!!.pause()
        super.onPause()
    }

    override fun onStop() {
        Log.d("MainActivity.java", "onStop()")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("MainActivity.java", "onDestroy()")
        super.onDestroy()
    }
}