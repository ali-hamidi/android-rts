package com.zeddic.war.level;

import com.zeddic.common.util.Polygon;
import com.zeddic.common.util.Polygon.PolygonBuilder;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * A single tile within the level.
 * 
 * @author baileys (Scott Bailey)
 */
public class LevelTile {  
  
  private TileType type;
  private int row;
  private int col;
  
  private static final Paint PAINT;
  private static final Polygon ROCK_SHAPE;
  
  static {
    PAINT = new Paint();
    PAINT.setColor(Color.RED);
    PAINT.setStyle(Paint.Style.STROKE);
    PAINT.setStrokeWidth(3);
    ROCK_SHAPE = new PolygonBuilder()
        .add(0, 0)
        .add(0, Level.TILE_SIZE)
        .add(Level.TILE_SIZE, Level.TILE_SIZE)
        .add(Level.TILE_SIZE, 0)
        .build();
  }
  
  public LevelTile(int row, int col) {
    this.row = row;
    this.col = col;
  }
  
  public void draw(Canvas c) {
    if (type == TileType.SOLID_ROCK) {
      c.drawRect(
          col * Level.TILE_SIZE,
          row * Level.TILE_SIZE, 
          (col * Level.TILE_SIZE) + Level.TILE_SIZE, 
          (row * Level.TILE_SIZE) + Level.TILE_SIZE,
          PAINT);
    }
  }
  
  public void update(long time) {
    
  }
  
  public int getRow() {
    return row;
  }
  
  public int getCol() {
    return col;
  }
  
  public void setType(TileType type) {
    this.type = type;
  }
  
  public TileType getType() {
    return type;
  }
}
