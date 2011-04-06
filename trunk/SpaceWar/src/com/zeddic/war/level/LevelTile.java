package com.zeddic.war.level;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.GameObject;
import com.zeddic.common.opengl.Sprite;
import com.zeddic.war.R;

/**
 * A single tile within the level.
 * 
 * @author baileys (Scott Bailey)
 */
public class LevelTile implements GameObject {  
  
  private TileType type;
  private int row;
  private int col;
  private static final Sprite sprite = new Sprite(32, 32, R.drawable.solid);
  
  public LevelTile(int row, int col) {
    this.row = row;
    this.col = col;
  }
  
  public void draw(GL10 gl) {
    
    if (type == TileType.SOLID_ROCK) {
      sprite.setLeft(col * Level.TILE_SIZE);
      sprite.setTop(row * Level.TILE_SIZE);
      sprite.draw(gl);
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
