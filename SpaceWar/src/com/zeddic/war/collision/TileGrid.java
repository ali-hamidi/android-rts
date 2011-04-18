package com.zeddic.war.collision;

import com.zeddic.common.Entity;
import com.zeddic.war.level.Level;
import com.zeddic.war.level.LevelTile;
import com.zeddic.war.level.TileType;

/**
 * A grid representing collision checks for the tilemap. 
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public class TileGrid {

  private float size;
  private final int cols;
  private final int rows;
	private TileCell[][] grid;
	
	public TileGrid(Level level) {
	  this.size = Level.TILE_SIZE;
	  this.rows = (int) (level.getHeight() / size) + 1;
	  this.cols = (int) (level.getWidth() / size) + 1;
	  
	  grid = new TileCell[rows][cols];
	  
	  for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {   
        TileCell cell = new TileCell(this, row, col, size);
        
        LevelTile tile = level.getTile(row, col);
        if (tile != null && tile.getType() != TileType.EMPTY) {
          cell.setBounds(TileBounds.SOLID);
        }
        grid[row][col] = cell;
      }
    }

		calculateEdges();
	}
	
	private void calculateEdges() {
	  for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {   
        grid[row][col].calculateEdges();
      }
    }
	}
	
	/**
	 * Collide a entity with any tiles in it's area.
	 */
	public boolean collide(CollideComponent component) {	 
	  Entity entity = component.entity;
	  
    int minCol = gridValue(entity.left());
    int maxCol = gridValue(entity.right());
    int minRow = gridValue(entity.top());
    int maxRow = gridValue(entity.bottom());
 
    boolean hit = false;
    
    for (int row = minRow; row <= maxRow; row++) {
      for(int col = minCol; col <= maxCol; col++) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
          hit = grid[row][col].collide(entity) || hit;
        }
      }
    }

    return hit;
	}
	
	 /**
   * Returns true if the selected entity is currently touching
   * any tile. Does not actually perform any collision resolution.
   */
  public boolean intersectsAnyTile(CollideComponent component) {
    Entity entity = component.entity;
    
    int minCol = gridValue(entity.left());
    int maxCol = gridValue(entity.right());
    int minRow = gridValue(entity.top());
    int maxRow = gridValue(entity.bottom());
 
    boolean hit = false;
    for (int row = minRow; row <= maxRow; row++) {
      for(int col = minCol; col <= maxCol; col++) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
          hit = grid[row][col].intersects(entity) || hit;
        }
      }
    }

    return hit;
  }

	private int gridValue(float rawValue) {
    return (int) Math.floor(rawValue / size);
  }
	
	public TileCell left(TileCell cell) {
    return get(cell.row, cell.col - 1);
  }
  
  public TileCell right(TileCell cell) {
    return get(cell.row, cell.col + 1);
  }
  
  public TileCell above(TileCell cell) {
    return get(cell.row - 1, cell.col);
  }
  
  public TileCell below(TileCell cell) {
    return get(cell.row + 1, cell.col);
  }
  
  public TileCell get(int row, int col) {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      return null;
    }
    
    return grid[row][col];
  }

  TileCell getCellForEntity(Entity entity) {
    int col = gridValue(entity.x);
    int row = gridValue(entity.y);

    return get(row, col);
  }
}
