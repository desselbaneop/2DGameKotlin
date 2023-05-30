package com.example.androidstudio2dgamedevelopment.map

import android.graphics.Canvas
import android.graphics.Rect
import com.example.androidstudio2dgamedevelopment.graphics.SpriteSheet

internal abstract class Tile(protected val mapLocationRect: Rect) {
    enum class TileType {
        WATER_TILE, LAVA_TILE, GROUND_TILE, GRASS_TILE, TREE_TILE
    }

    abstract fun draw(canvas: Canvas)

    companion object {
        fun getTile(idxTileType: Int, spriteSheet: SpriteSheet, mapLocationRect: Rect): Tile? {
            return when (TileType.values()[idxTileType]) {
                TileType.WATER_TILE -> WaterTile(spriteSheet, mapLocationRect)
                TileType.LAVA_TILE -> LavaTile(spriteSheet, mapLocationRect)
                TileType.GROUND_TILE -> GroundTile(spriteSheet, mapLocationRect)
                TileType.GRASS_TILE -> GrassTile(spriteSheet, mapLocationRect)
                TileType.TREE_TILE -> TreeTile(spriteSheet, mapLocationRect)
                else -> null
            }
        }
    }
}