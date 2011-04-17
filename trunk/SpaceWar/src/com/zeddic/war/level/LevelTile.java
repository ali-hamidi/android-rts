package com.zeddic.war.level;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.GameObject;
import com.zeddic.common.opengl.Color;
import com.zeddic.common.opengl.Sprite;
import com.zeddic.war.R;

/**
 * A single tile within the level.
 * 
 * @author baileys (Scott Bailey)
 */
public class LevelTile implements GameObject {  

  private static final Sprite solid = new Sprite(32, 32, R.drawable.border);
  private static final Sprite blue = new Sprite(32, 32, R.drawable.solid);
  private static final Color color = new Color(0, 76, 255, 255);
  private int row;
  private int col;
  private Sprite activeSprite;
  public TileType type;

  public LevelTile(int row, int col) {
    this.row = row;
    this.col = col;
    
    blue.setColor(color);
  }
  
  public void draw(GL10 gl) {

    if (type == TileType.SOLID_ROCK) {
      activeSprite = blue;
    } else if (type == TileType.EDGE) {
      activeSprite = solid;
    } else {
      return;
    }

    activeSprite.setLeft(col * Level.TILE_SIZE);
    activeSprite.setTop(row * Level.TILE_SIZE);
    activeSprite.draw(gl);
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
