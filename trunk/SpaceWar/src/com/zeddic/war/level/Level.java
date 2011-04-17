package com.zeddic.war.level;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.GameObject;
import com.zeddic.common.opengl.Screen;


public class Level implements GameObject {

  public static final int TILE_SIZE = 32;
  private final LevelTile[][] grid;
  public Map map;
  private int rows;
  private int cols;
  
  public Level(LevelBuilder builder) {
    this.rows = builder.rows;
    this.cols = builder.cols;
    this.grid = builder.grid;
    map = new Map(rows, cols);
  }
  
  @Override
  public void update(long time) {
    map.update(time);
    for (int row = 0; row < rows; row++) {
      for(int col = 0; col < cols; col++) {
        LevelTile tile = grid[row][col];
        if (tile != null) {
          tile.update(time);
        }
      }
    }
  }
  
  @Override
  public void draw(GL10 gl) {
    map.draw(gl);
    
    gl.glPushMatrix();
    
    for (int row = 0; row < rows; row++) {
      for(int col = 0; col < cols; col++) {
        LevelTile tile = grid[row][col];
        if (tile != null && tile.type != TileType.EMPTY) {
          tile.draw(gl);
        }
      }
    }
    
    gl.glPopMatrix();
  }
  
  @Override
  public void reset() {
    // Nothing to reset.
  }
  
  public int getTileRows() {
    return rows;
  }
  
  public int getTileCols() {
    return cols;
  }
  
  public float getWidth() {
    return map.width;
  }
  
  public float getHeight() {
    return map.height;
  }
  
  public LevelTile getTile(int row, int col) {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      return null;
    }
    
    return grid[row][col];
  }
  
  /*private int convertMapToGridValue(float rawValue) {
    return (int) Math.floor(rawValue / TILE_SIZE);
  }
  
  public LevelTile left(LevelTile cell) {
    return get(cell.row, cell.col - 1);
  }
  
  public LevelTile right(LevelTile cell) {
    return get(cell.row, cell.col + 1);
  }
  
  public LevelTile above(LevelTile cell) {
    return get(cell.row - 1, cell.col);
  }
  
  public LevelTile below(LevelTile cell) {
    return get(cell.row + 1, cell.col);
  }
  
  public LevelTile get(int row, int col) {
    if (row < 0 || row >= height || col < 0 || col >= width) {
      return null;
    }
    
    return grid[row][col];
  }*/
  
  public static class LevelBuilder {
    private int BUFFER = 1;
    
    private LevelTile[][] grid;
    private int rows;
    private int cols;
    

    public LevelBuilder() {}

    public void withGridSize(int rows, int cols) {
      this.rows = rows + BUFFER * 2;
      this.cols = cols + BUFFER * 2;
      grid = new LevelTile[this.rows][this.cols];
      
      // Fill the border of the world with a solid buffer.
      for (int i = 0; i < BUFFER; i++) {
        fillRow(i, TileType.EDGE);
        fillRow(this.rows - 1 - i, TileType.EDGE);
        
        fillCol(i, TileType.EDGE);
        fillCol(this.cols - 1 - i, TileType.EDGE);
      }
    }

    private void fillCol(int col, TileType type) {
      for (int row = 0; row < rows; row++) {
        setRawTile(row, col, type);
      }
    }

    private void fillRow(int row, TileType type) {
      for (int col = 0; col < cols; col++) {
        setRawTile(row, col, type);
      }
    }

    public void addTile(int row, int col, TileType type) {
      setRawTile(row + BUFFER, col + BUFFER, type);
    }
    
    private void setRawTile(int row, int col, TileType type) {
      LevelTile tile = new LevelTile(row, col);
      tile.setType(type);
      
      grid[row][col] = tile;
    }
    
    public Level build() {
      return new Level(this);
    }
  }
}
