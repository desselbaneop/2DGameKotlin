package com.example.androidstudio2dgamedevelopment.map

import android.graphics.Canvas
import android.graphics.Rect
import com.example.androidstudio2dgamedevelopment.graphics.Sprite
import com.example.androidstudio2dgamedevelopment.graphics.SpriteSheet

internal class LavaTile(spriteSheet: SpriteSheet, mapLocationRect: Rect) : Tile(mapLocationRect) {
    private val sprite: Sprite?

    init {
        sprite = spriteSheet.lavaSprite
    }

    override fun draw(canvas: Canvas) {
        sprite!!.draw(canvas, mapLocationRect.left, mapLocationRect.top)
    }
}