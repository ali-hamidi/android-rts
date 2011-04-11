package com.zeddic.war.collision;

import com.zeddic.common.Entity;
import com.zeddic.common.util.SimpleList;
import com.zeddic.war.level.Level;

/**
 * A collision grid holding entities that reside in the game world.
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public class EntityGrid {

  private float size;
  private final int cols;
  private final int rows;
	private EntityCell[][] grid;
	
	/**
	 * Creates a new collision grid for world objects, with each
	 * cell of the specified size.
	 */
	public EntityGrid(Level level, float size) {

	  this.size = size;
	  this.rows = (int) (level.getHeight() / size) + 1;
	  this.cols = (int) (level.getWidth() / size) + 1;
	  
	  grid = new EntityCell[rows][cols];

	  for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        grid[row][col] = new EntityCell(row, col);
      }
    }
	}
	
	/**
	 * Collides an entity with any other objects cells that it resides in.
	 */
	public void collide(CollideComponent component) {	 
	  Entity entity = component.entity;
	  
    int minCol = gridValue(entity.left());
    int maxCol = gridValue(entity.right());
    int minRow = gridValue(entity.top());
    int maxRow = gridValue(entity.bottom());
 
    for (int row = minRow; row <= maxRow; row++) {
      for(int col = minCol; col <= maxCol; col++) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
          grid[row][col].collide(entity);
        }
      }
    }
	}

	/**
	 * Fetches a set of cells that are 'relevant' for collision checks for a given
	 * entity. Results are stored in the array <code>result</code> with the number
	 * of results in the array returned as an int.
	 */
  public int getReleventCells(CollideComponent component, SimpleList<EntityCell> result) {
    Entity entity = component.entity;
    
    int minCol = gridValue(entity.left());
    int maxCol = gridValue(entity.right());
    int minRow = gridValue(entity.top());
    int maxRow = gridValue(entity.bottom());
    
    return getCells(minRow, minCol, maxRow, maxCol, result);
  }
 
  /**
   * Fetches all cells that are within a given radius of the a set world position.
   * Results are stored in the array <code>result</code> with the number
   * of results in the array returned as an int.
   */
  public int getCellsWithinRadius(
      float worldX,
      float worldY,
      float radius,
      SimpleList<EntityCell> result) {

    int minRow = gridValue(worldY - radius);
    int maxRow = gridValue(worldY + radius);
    int minCol = gridValue(worldX - radius);
    int maxCol = gridValue(worldX + radius);
    
    return getCells(minRow, minCol, maxRow, maxCol, result);
  }
  
  /**
   * Places all cells within the given range into the supplied result array. The
   * number of results is returned as an int.
   */
  public int getCells(int minRow, int minCol, int maxRow, int maxCol, SimpleList<EntityCell> result) {
    
    result.clear();

    EntityCell cell;
    int hits = 0;
    for (int col = minCol; col <= maxCol; col++) {
      for (int row = minRow; row <= maxRow; row++) {
        cell = get(row, col);
        if (cell != null) {
          result.add(cell);
          hits++;
        }
      }
    }
    
    return hits;
  }
	
	private int gridValue(float rawValue) {
    return (int) Math.floor(rawValue / size);
  }
	
	public EntityCell left(EntityCell cell) {
    return get(cell.row, cell.col - 1);
  }
  
  public EntityCell right(EntityCell cell) {
    return get(cell.row, cell.col + 1);
  }
  
  public EntityCell above(EntityCell cell) {
    return get(cell.row - 1, cell.col);
  }
  
  public EntityCell below(EntityCell cell) {
    return get(cell.row + 1, cell.col);
  }
  
  public EntityCell get(int row, int col) {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      return null;
    }
    return grid[row][col];
  }
  
  /**
   * Updates an objects position in the grid.
   */
  public void update(CollideComponent component) {
    remove(component);
    add(component);
  }

  /**
   * Adds an entity into the collision system.
   */
  public void add(CollideComponent component) {
    Entity entity = component.entity;
    for (int col = gridValue(entity.left()); col <= gridValue(entity.right()); col++) {
      for (int row = gridValue(entity.top()); row <= gridValue(entity.bottom()); row++) {
        EntityCell cell = get(row, col);
        if (cell != null) {
          cell.add(entity);
          component.currentCells.add(cell);
        }
      }
    }
  }
  
  /**
   * Removes an object from the grid entirely.
   */
  public void remove(CollideComponent component) {
    Entity entity = component.entity;
 
    int length = component.currentCells.size;
    for (int i = 0; i < length; i++) {
      component.currentCells.items[i].remove(entity);
    }
    component.currentCells.clear();
  }
}
