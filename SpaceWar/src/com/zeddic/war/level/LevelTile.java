package com.zeddic.war.level;

import android.graphics.Canvas;

/**
 * A single tile within the level.
 * 
 * @author baileys (Scott Bailey)
 */
public class LevelTile {  
  
  private TileType type;
  private int row;
  private int col;
  
  public LevelTile(int row, int col) {
    this.row = row;
    this.col = col;
  }
  
  public void draw(Canvas c) {
   
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
