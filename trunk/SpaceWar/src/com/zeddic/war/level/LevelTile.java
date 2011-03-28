package com.zeddic.war.level;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Color;
import android.graphics.Paint;

import com.zeddic.common.GameObject;
import com.zeddic.common.opengl.WireFrame;
import com.zeddic.common.util.Polygon;
import com.zeddic.common.util.Polygon.PolygonBuilder;
import com.zeddic.war.GameState;

/**
 * A single tile within the level.
 * 
 * @author baileys (Scott Bailey)
 */
public class LevelTile implements GameObject {  
  
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
  
  // This is a god awful way of rendering wireframes.. but its a temp solution
  // of seeing the 3d shapes until some textures can be put in.
  private static final WireFrame cube = new WireFrame.Builder()
      .add(-1, -1, -1)
      .add( 1, -1, -1)
      .add( 1,  1, -1)
      .add(-1,  1, -1)
      .add(-1, -1, -1)
      .add(-1,  1, -1)
      .add(-1,  1,  1)
      .add(-1, -1,  1)
      .add( 1, -1,  1)
      .add( 1,  1,  1)
      .add(-1,  1,  1)
      .add(-1, -1,  1)
      .add(-1, -1, -1)
      .add( 1, -1, -1)
      .add( 1, -1,  1)
      .add( 1,  1,  1)
      .add( 1,  1, -1)
      .build();
  
  public LevelTile(int row, int col) {
    this.row = row;
    this.col = col;
  }
  
  public void draw(GL10 gl) {
    
    // TODO(baileys): Open flips the standard concept of the y axis.
    // As a result, the levels appear flipped here. This class will
    // need to become aware of the max # of rows to inverse y, or 
    // we'll need to change the order of our mapping matrix.
    
    if (type == TileType.SOLID_ROCK) {
      cube.scale = Level.TILE_SIZE / 2;
      cube.x = col * Level.TILE_SIZE + Level.TILE_SIZE / 2;
      cube.y = row * Level.TILE_SIZE + Level.TILE_SIZE / 2;
      cube.draw(gl);
    }
  }

  @Override
  public void reset() {
    // Nothing to do.
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
