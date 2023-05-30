package com.example.androidstudio2dgamedevelopment.map

import android.graphics.Canvas
import android.graphics.Rect
import com.example.androidstudio2dgamedevelopment.graphics.Sprite
import com.example.androidstudio2dgamedevelopment.graphics.SpriteSheet

internal class TreeTile(spriteSheet: SpriteSheet, mapLocationRect: Rect) : Tile(mapLocationRect) {
    private val grassSprite: Sprite?
    private val treeSprite: Sprite?

    init {
        grassSprite = spriteSheet.grassSprite
        treeSprite = spriteSheet.treeSprite
    }

    override fun draw(canvas: Canvas) {
        grassSprite!!.draw(canvas, mapLocationRect.left, mapLocationRect.top)
        treeSprite!!.draw(canvas, mapLocationRect.left, mapLocationRect.top)
    }
}