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
	public void collide(CollideComponent component) {	 
	  Entity entity = component.entity;
	  
    int minCol = convertMapToGridValue(entity.left());
    int maxCol = convertMapToGridValue(entity.right());
    int minRow = convertMapToGridValue(entity.top());
    int maxRow = convertMapToGridValue(entity.bottom());
 
    for (int row = minRow; row <= maxRow; row++) {
      for(int col = minCol; col <= maxCol; col++) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
          grid[row][col].collide(entity);
        }
      }
    }
	}

	private int convertMapToGridValue(float rawValue) {
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
    int col = convertMapToGridValue(entity.x);
    int row = convertMapToGridValue(entity.y);

    return get(row, col);
  }
}
