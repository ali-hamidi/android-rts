package com.zeddic.war.level;

import android.graphics.Canvas;


public class Level {

  public static final int TILE_SIZE = 32;
  private final LevelTile[][] grid;
  public Map map;
  private int rows;
  private int cols;
  
  public Level(LevelBuilder builder) {
    this.grid = builder.grid;
    map = new Map();
    map.setSize(builder.width, builder.height);
    this.rows = builder.rows;
    this.cols = builder.cols;
  }
  
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
  
  public void draw(Canvas c) {
    map.draw(c);
    for (int row = 0; row < rows; row++) {
      for(int col = 0; col < cols; col++) {
        LevelTile tile = grid[row][col];
        if (tile != null) {
          tile.draw(c);
        }
      }
    }
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
    private LevelTile[][] grid;
    private float width;
    private float height;
    private int rows;
    private int cols;
    
    
    public LevelBuilder() {}
    
    public void withSize(float width, float height) {
      this.width = width;
      this.height = height;
      this.rows = (int) (height / TILE_SIZE);
      this.cols = (int) (width / TILE_SIZE);
      grid = new LevelTile[rows][cols];
    }
    
    public void addTile(int row, int col, TileType type) {
      LevelTile tile = new LevelTile(row, col);
      tile.setType(type);
      grid[row][col] = tile;
    }
    
    public Level build() {
      return new Level(this);
    }
  }
}
